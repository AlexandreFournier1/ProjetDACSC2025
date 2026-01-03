package hepl.dacsc;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Logger;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Protocol;
import hepl.dacsc.lib.MRPS;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.security.Security;

public class ServerMRPS {

    // TODO : lire depuis un fichier properties
    public static final int PORT_REPORT_SECURE = 60000;

    public static void main(String[] args) throws IOException {
        try {
            // Initialisation Bouncy Castle
            Security.addProvider(new BouncyCastleProvider());

            Protocol protocol = new MRPS();

            Logger logger = message ->
                    System.out.println("[SERVER MRPS] " + message);

            ThreadServeurDemande server = new ThreadServeurDemande(PORT_REPORT_SECURE, protocol, logger);

            server.start();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
