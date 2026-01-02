package hepl.dacsc.utils;

import java.io.FileInputStream;
import java.security.KeyStore;

public class KeystoreUtils {
    public static KeyStore loadKeystore(String path, String password) throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream(path)) {
            ks.load(fis, password.toCharArray());
        }
        return ks;
    }
}
