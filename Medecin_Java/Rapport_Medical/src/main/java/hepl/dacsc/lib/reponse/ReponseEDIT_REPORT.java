package hepl.dacsc.lib.reponse;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Reponse;

import java.io.Serializable;

public class ReponseEDIT_REPORT implements Reponse, Serializable {

    private final boolean success;

    public ReponseEDIT_REPORT(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
