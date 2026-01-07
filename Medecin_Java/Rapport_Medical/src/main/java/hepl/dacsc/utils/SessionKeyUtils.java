package hepl.dacsc.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Security;

public class SessionKeyUtils {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static SecretKey generateSessionKey() throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance("DES", "BC");
        kg.init(56);
        return kg.generateKey();
    }
}
