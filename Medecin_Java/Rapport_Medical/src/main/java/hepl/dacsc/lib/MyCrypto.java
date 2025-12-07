package hepl.dacsc.lib;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class MyCrypto {
    // Cryptage Décryptage Syméthrique
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
}
