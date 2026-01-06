package hepl.dacsc.lib;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

public class MyCrypto {
    // Cryptage Décryptage Symétrique
    public static byte[] CryptSymDES(SecretKey key, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Cipher chiffrementE =  Cipher.getInstance("DES/ECB/PKCS5Padding","BC");
        chiffrementE.init(Cipher.ENCRYPT_MODE, key);
        return chiffrementE.doFinal(data);
    }

    public static byte[] DecryptSymDES(SecretKey key, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher chiffrementD =  Cipher.getInstance("DES/ECB/PKCS5Padding","BC");
        chiffrementD.init(Cipher.DECRYPT_MODE, key);
        return chiffrementD.doFinal(data);
    }

    public static byte[] computeHmac(SecretKey key, byte[] data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256", "BC");
        mac.init(key);
        return mac.doFinal(data);
    }

    public static byte[] CryptAsymRSA(byte[] data, PublicKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    public static byte[] DecryptAsymRSA(byte[] data, PrivateKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    public static SecretKey rebuildAESKey(byte[] rawKey) {
        return new SecretKeySpec(rawKey, "AES");
    }
}
