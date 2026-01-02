package hepl.dacsc.utils;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;

public class KeyUtils {
    public static PrivateKey getPrivateKey(KeyStore ks, String alias, String keyPassword) throws Exception {
        return (PrivateKey) ks.getKey(alias, keyPassword.toCharArray());
    }

    public static PublicKey getPublicKey(KeyStore ks, String alias) throws Exception {
        return ks.getCertificate(alias).getPublicKey();
    }
}