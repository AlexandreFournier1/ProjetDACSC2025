package hepl.dacsc.lib.reponse;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Reponse;

public class ReponseADD_PATIENT implements Reponse {
    private int id;

    public ReponseADD_PATIENT(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
