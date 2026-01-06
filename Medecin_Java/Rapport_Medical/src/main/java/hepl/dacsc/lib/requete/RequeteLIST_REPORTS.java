package hepl.dacsc.lib.requete;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Requete;

public class RequeteLIST_REPORTS implements Requete {
    private Integer patientId;

    public RequeteLIST_REPORTS(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getPatientId() {
        return patientId;
    }
}
