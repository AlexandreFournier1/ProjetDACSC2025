package hepl.dacsc.lib.requete;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Requete;

public class RequeteLOGOUT implements Requete {
    private final int doctorId;

    public RequeteLOGOUT(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getDoctorId() {
        return doctorId;
    }
}
