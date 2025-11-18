package hepl.dacsc.lib.reponse;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Reponse;

public class ReponseADD_CONSULTATION implements Reponse {
    public boolean over17hours;

    public ReponseADD_CONSULTATION(boolean over17hours) {
        this.over17hours = over17hours;
    }

    public boolean isOver17hours() {
        return over17hours;
    }
}
