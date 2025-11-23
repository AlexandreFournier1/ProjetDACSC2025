package hepl.dacsc.lib.requete;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Requete;

public class RequeteDELETE_CONSULTATION implements Requete {
    private Integer consultationId;

    public RequeteDELETE_CONSULTATION(Integer consultationId) {
        this.consultationId = consultationId;
    }
    public Integer getConsultationId() {
        return consultationId;
    }
}
