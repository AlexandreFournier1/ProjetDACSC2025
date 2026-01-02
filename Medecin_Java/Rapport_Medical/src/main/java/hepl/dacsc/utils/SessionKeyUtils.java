package hepl.dacsc.utils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class SessionKeyUtils {
    public static SecretKey generateSessionKey() throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(128);
        return kg.generateKey();
    }
}
