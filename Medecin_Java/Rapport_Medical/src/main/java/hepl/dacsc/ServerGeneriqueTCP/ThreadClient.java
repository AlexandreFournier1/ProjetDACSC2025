package hepl.dacsc.ServerGeneriqueTCP;

import hepl.dacsc.ServerGeneriqueTCP.exceptions.FinConnexionException;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Logger;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Protocol;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Reponse;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Requete;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class ThreadClient extends Thread {
    protected Protocol protocol;
    protected Socket csocket;
    protected Logger logger;
    private int num;
    private static int numCourant = 1;

    public ThreadClient(Protocol protocol, Socket csocket, Logger logger) throws IOException
    {
        super("TH Client " + numCourant + " (protocole=" + protocol.getNom() + ")");
        this.protocol = protocol;
        this.csocket = csocket;
        this.logger = logger;
        this.num = numCourant++;
    }

    public ThreadClient(Protocol protocol, ThreadGroup groupe, Logger logger) throws IOException
    {
        super(groupe,"TH Client " + numCourant + " (protocole=" + protocol.getNom() + ")");
        this.protocol = protocol;
        this.csocket = null;
        this.logger = logger;
        this.num = numCourant++;
    }

    @Override
    public void run() {
        try
        {
            ObjectOutputStream oos = null;
            ObjectInputStream  ois = null;

            try
            {
                ois = new ObjectInputStream(csocket.getInputStream());
                oos = new ObjectOutputStream(csocket.getOutputStream());

                while (true)
                {
                    Requete requete = (Requete) ois.readObject();
                    Reponse reponse = protocol.TraiteRequete(requete,csocket);
                    oos.writeObject(reponse);
                }
            }
            catch (FinConnexionException ex)
            {
                logger.Trace("Fin connexion demandée par protocole");
                if (oos != null && ex.getReponse() != null)
                    oos.writeObject(ex.getReponse());
            }
        }
        catch (IOException ex) { logger.Trace("Erreur I/O"); }
        catch (ClassNotFoundException ex) { logger.Trace("Erreur requete invalide");
        }
        finally
        {
            try { csocket.close(); }
            catch (IOException ex) { logger.Trace("Erreur fermeture socket"); }
        }
    }
}
