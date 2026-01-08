package hepl.dacsc.lib.requete;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Requete;

import java.io.Serializable;

public class RequeteEDIT_REPORT implements Requete, Serializable {

    private final int reportId;
    private final byte[] encryptedReport;

    public RequeteEDIT_REPORT(int reportId, byte[] encryptedReport) {
        this.reportId = reportId;
        this.encryptedReport = encryptedReport;
    }

    public int getReportId() {
        return reportId;
    }

    public byte[] getEncryptedReport() {
        return encryptedReport;
    }
}
