package hepl.dacsc.lib.requete;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Requete;

public class RequeteGET_CONSULTATION implements Requete {
    private int doctorId;

    public RequeteGET_CONSULTATION(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getDoctorId() {
        return doctorId;
    }
}
