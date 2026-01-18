package hepl.dacsc.lib.reponse;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Reponse;

public class ReponseLOGOUT implements Reponse {
    private final boolean ok;

    public ReponseLOGOUT(boolean ok) {
        this.ok = ok;
    }

    public boolean isOk() {
        return ok;
    }
}
