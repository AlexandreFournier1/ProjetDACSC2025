package hepl.dacsc;

import hepl.dacsc.ServerGeneriqueTCP.ThreadClient;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Logger;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Protocol;

import java.io.IOException;
import java.net.Socket;

public class ThreadClientDemande extends ThreadClient {

    public ThreadClientDemande(Protocol protocol, Socket csocket, Logger logger)
            throws IOException {
        super(protocol, csocket, logger);
    }

    @Override
    public void run() {
        logger.Trace("TH Client (Demande) démarre...");
        super.run();
        logger.Trace("TH Client (Demande) se termine.");
    }
}
