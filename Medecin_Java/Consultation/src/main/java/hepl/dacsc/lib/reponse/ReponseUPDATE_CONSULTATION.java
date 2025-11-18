package hepl.dacsc.lib.reponse;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Reponse;

public class ReponseUPDATE_CONSULTATION implements Reponse {
    private boolean valid;

    public ReponseUPDATE_CONSULTATION(boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        return valid;
    }
}
