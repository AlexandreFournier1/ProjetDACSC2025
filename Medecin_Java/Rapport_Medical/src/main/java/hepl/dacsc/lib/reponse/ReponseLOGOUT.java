package hepl.dacsc.lib.reponse;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Reponse;

public class ReponseLOGOUT implements Reponse {
    private final boolean success;

    public ReponseLOGOUT(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}