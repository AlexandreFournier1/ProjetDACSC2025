package hepl.dacsc.lib.reponse;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Reponse;

public class ReponseLOGIN implements Reponse {
    private boolean valid;
    public ReponseLOGIN(boolean valid) {
        this.valid = valid;
    }
    public boolean isValid() {
        return valid;
    }
}
