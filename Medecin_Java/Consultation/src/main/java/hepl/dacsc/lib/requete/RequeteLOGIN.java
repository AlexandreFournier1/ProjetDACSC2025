package hepl.dacsc.lib.requete;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Reponse;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Requete;

public class RequeteLOGIN implements Requete {
    private Integer Id;
    private String LastName;
    private String FirstName;
    private String Mdp;

    public RequeteLOGIN(int id, String LastName, String FirstName, String Mdp) {
        this.Id = id;
        this.LastName = LastName;
        this.FirstName = FirstName;
        this.Mdp = Mdp;
    }

    public Integer getId() {
        return Id;
    }

    public String getLastName() {
        return LastName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getMdp() {
        return Mdp;
    }
}
