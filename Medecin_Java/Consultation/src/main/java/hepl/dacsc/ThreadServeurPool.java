package hepl.dacsc;

import hepl.dacsc.ServerGeneriqueTCP.FileAttente;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Logger;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Protocol;
import hepl.dacsc.ServerGeneriqueTCP.ThreadServer;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ThreadServeurPool extends ThreadServer {
    private FileAttente connexionsEnAttente;
    private ThreadGroup pool;
    private int taillePool;

    public ThreadServeurPool(int port, Protocol protocol, int taillePool, Logger logger) throws IOException {
        super(port, protocol, logger);

        connexionsEnAttente = new FileAttente();
        pool = new ThreadGroup("POOL");
        this.taillePool = taillePool;
    }

    @Override
    public void run() {
        logger.Trace("Démarrage du TH Serveur (Pool)...");
        // Création du pool de threads
        try {
            ThreadClientPool th;
            for (int i = 0; i < taillePool; i++) {
                th = new ThreadClientPool(
                        protocol,
                        connexionsEnAttente,
                        pool,
                        logger
                );
                th.start();
            }

        } catch (IOException ex) {
            logger.Trace("Erreur I/O lors de la création du pool de threads");
            return;
        }

        // Attente des connexions
        while (!isInterrupted()) {
            try {
                Socket csocket = ssocket.accept();
                logger.Trace("Connexion acceptée");
                connexionsEnAttente.addConnexion(csocket);
            } catch (SocketTimeoutException ignored) {
            } catch (IOException e) {
                logger.Trace("Erreur I/O serveur");
            }
        }

        logger.Trace("TH Serveur (Pool) interrompu.");
        pool.interrupt();
    }
}
