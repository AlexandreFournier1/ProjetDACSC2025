package hepl.dacsc;

import hepl.dacsc.ServerGeneriqueTCP.FileAttente;
import hepl.dacsc.ServerGeneriqueTCP.ThreadClient;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Logger;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Protocol;

import java.io.IOException;

public class ThreadClientPool extends ThreadClient {
    private FileAttente connexionsEnAttente;

    public ThreadClientPool(Protocol protocol, FileAttente file, ThreadGroup groupe, Logger logger) throws IOException
    {
        super(protocol, groupe, logger);
        connexionsEnAttente = file;
    }

    @Override
    public void run()
    {
        logger.Trace("TH Client (Pool) démarre...");

        ClientMRPS mf = new ClientMRPS();
        mf.setVisible(true);

        boolean interrompu = false;
        while(!interrompu)
        {
            try
            {
                logger.Trace("Attente d'une connexion...");
                csocket = connexionsEnAttente.getConnexion();
                logger.Trace("Connexion prise en charge.");
                super.run();
            }
            catch (InterruptedException ex)
            {
                logger.Trace("Demande d'interruption...");
                interrompu = true;
            }
        }
        logger.Trace("TH Client (Pool) se termine.");
    }
}
