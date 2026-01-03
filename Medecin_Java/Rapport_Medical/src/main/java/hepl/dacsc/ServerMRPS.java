package hepl.dacsc;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Logger;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Protocol;
import hepl.dacsc.lib.MRPS;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.Security;
import java.util.Properties;

public class ServerMRPS {

    public static void main(String[] args) throws IOException {
        try {
            Security.addProvider(new BouncyCastleProvider());

            Protocol protocol = new MRPS();

            Logger logger = message ->
                    System.out.println("[SERVER MRPS] " + message);

            Properties props = new Properties();
            props.load(new FileInputStream("src/main/java/hepl/dacsc/config.properties"));

            int port = Integer.parseInt(props.getProperty("PORT_REPORT_SECURE"));

            ThreadServeurDemande server = new ThreadServeurDemande(port, protocol, logger);

            server.start();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
