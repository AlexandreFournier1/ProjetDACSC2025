package hepl.dacsc.lib.requete;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Requete;

import java.io.Serializable;
import java.time.LocalDate;

public class RequeteADD_REPORT implements Requete, Serializable {
    private final int patientId;
    private final LocalDate reportDate;
    private final byte[] encryptedReport;
    private final byte[] signature;

    public RequeteADD_REPORT(int patientId, LocalDate reportDate, byte[] encryptedReport, byte[] signature) {
        this.patientId = patientId;
        this.reportDate = reportDate;
        this.encryptedReport = encryptedReport;
        this.signature = signature;
    }

    public int getPatientId() {
        return patientId;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public byte[] getEncryptedReport() {
        return encryptedReport;
    }

    public byte[] getSignature() {
        return signature;
    }
}
