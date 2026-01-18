package hepl.dacsc;

import hepl.dacsc.ServerGeneriqueTCP.FileAttente;
import hepl.dacsc.ServerGeneriqueTCP.ThreadClient;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Logger;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Protocol;

import java.io.IOException;

public class ThreadClientPool extends ThreadClient {

    private final FileAttente connexionsEnAttente;

    public ThreadClientPool(
            Protocol protocol,
            FileAttente file,
            ThreadGroup groupe,
            Logger logger) throws IOException {
        super(protocol, groupe, logger);
        this.connexionsEnAttente = file;
    }

    @Override
    public void run() {
        logger.Trace("TH Client (Pool) démarré");

        boolean interrupted = false;

        while (!interrupted) {
            try {
                logger.Trace("Attente connexion...");
                csocket = connexionsEnAttente.getConnexion();
                logger.Trace("Connexion prise en charge : " + csocket);

                // lance le traitement classique ThreadClient
                super.run();

            } catch (InterruptedException e) {
                interrupted = true;
            }
        }

        logger.Trace("TH Client (Pool) terminé");
    }
}
