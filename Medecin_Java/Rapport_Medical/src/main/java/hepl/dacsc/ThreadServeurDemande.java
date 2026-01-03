package hepl.dacsc;

import hepl.dacsc.ServerGeneriqueTCP.ThreadServer;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Logger;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Protocol;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ThreadServeurDemande extends ThreadServer {

    public ThreadServeurDemande(int port, Protocol protocol, Logger logger)
            throws IOException {
        super(port, protocol, logger);
    }

    @Override
    public void run() {
        logger.Trace("Démarrage du TH Serveur (Demande)...");

        while (!this.isInterrupted()) {
            try {
                ssocket.setSoTimeout(2000);
                Socket csocket = ssocket.accept();

                logger.Trace("Connexion acceptée, création TH Client");

                Thread th = new ThreadClientDemande(protocol, csocket, logger);
                th.start();

            } catch (SocketTimeoutException ex) {
            } catch (IOException ex) {
                logger.Trace("Erreur I/O");
            }
        }

        logger.Trace("TH Serveur (Demande) interrompu.");
        try {
            ssocket.close();
        } catch (IOException ex) {
            logger.Trace("Erreur fermeture ServerSocket");
        }
    }
}
