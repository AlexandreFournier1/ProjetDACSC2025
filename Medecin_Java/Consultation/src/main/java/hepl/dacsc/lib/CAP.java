package hepl.dacsc.lib;

import hepl.dacsc.ServerGeneriqueTCP.exceptions.FinConnexionException;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Protocol;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Reponse;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Requete;

import java.net.Socket;

public class CAP implements Protocol {
    @Override
    public String getNom() {
        return "CAP";
    }

    @Override
    public synchronized Reponse TraiteRequete(Requete requete, Socket socket) throws FinConnexionException
    {
        // C'est ici qu'on va rediriger en fonction de la requête reçue
        return null;
    }
}
