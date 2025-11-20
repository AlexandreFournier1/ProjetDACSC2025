package hepl.dacsc;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Logger;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Protocol;
import hepl.dacsc.lib.CAP;

import java.io.IOException;

public class ServerCAP {
    public static void main(String[] args) throws IOException {
        try{
            Protocol protocol = new CAP();
            Logger logger = new Logger() {
                @Override
                public void Trace(String message) {
                    System.out.println("[SERVER] " + message);
                }
            };

            ThreadServeurPool server = new ThreadServeurPool(50000, protocol, 2, logger);

            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
