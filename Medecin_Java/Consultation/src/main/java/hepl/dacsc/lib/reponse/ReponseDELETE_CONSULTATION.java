package hepl.dacsc.lib.reponse;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Reponse;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Requete;

public class ReponseDELETE_CONSULTATION implements Reponse {
    private boolean valid;
    public ReponseDELETE_CONSULTATION(boolean valid) {
        this.valid = valid;
    }
    public boolean isValid() {
        return valid;
    }
}
