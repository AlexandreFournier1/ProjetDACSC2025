package hepl.dacsc.lib.requete;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Requete;

public class RequeteADD_PATIENT implements Requete {
    private String nom;
    private String prenom;

    public RequeteADD_PATIENT(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }
}
