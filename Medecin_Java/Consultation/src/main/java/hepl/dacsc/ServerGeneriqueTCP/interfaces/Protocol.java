package hepl.dacsc.ServerGeneriqueTCP.interfaces;

import hepl.dacsc.ServerGeneriqueTCP.exceptions.FinConnexionException;

import java.net.Socket;

public interface Protocol {
    String getNom();
    Reponse TraiteRequete(Requete requete, Socket socket) throws FinConnexionException;
}
