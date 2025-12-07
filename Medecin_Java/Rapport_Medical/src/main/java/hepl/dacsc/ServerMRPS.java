package hepl.dacsc;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Logger;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Protocol;
import hepl.dacsc.lib.MRPS;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.security.Security;

public class ServerMRPS {

    // Pas oublier le fichier properties pour tout ce qui est port etc...
    public static final int PORT_REPORT_SECURE = 60000;

    public static void main(String[] args) throws IOException {
        try{
            Security.addProvider(new BouncyCastleProvider());
            Protocol protocol = new MRPS();
            Logger logger = new Logger() {
                @Override
                public void Trace(String message) {
                    System.out.println("[SERVER] " + message);
                }
            };

            ThreadServeurPool server = new ThreadServeurPool(PORT_REPORT_SECURE, protocol, 1, logger);

            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
