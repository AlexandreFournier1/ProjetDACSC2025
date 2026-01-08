package hepl.dacsc.lib.reponse;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Reponse;

public class ReponseADD_REPORT implements Reponse {
    private boolean success;

    public ReponseADD_REPORT(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

}
