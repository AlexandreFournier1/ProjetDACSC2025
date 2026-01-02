package hepl.dacsc.lib.requete;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Requete;

// Première requête envoyée au serveur
public class RequeteLOGIN implements Requete {
    private String firstname;
    private String lastname;

    public RequeteLOGIN(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }
}
