package hepl.dacsc.lib.reponse;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Reponse;

public class ReponseLOGIN_DIGEST implements Reponse {
    private boolean success;

    public ReponseLOGIN_DIGEST(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
