package hepl.dacsc.ServerGeneriqueTCP.exceptions;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Reponse;

public class FinConnexionException extends Exception {
    private Reponse reponse;

    public FinConnexionException(Reponse reponse) {
        super("Fin de connexion dédiée par protocole");
        this.reponse = reponse;
    }

    public Reponse getReponse() {
        return reponse;
    }
}
