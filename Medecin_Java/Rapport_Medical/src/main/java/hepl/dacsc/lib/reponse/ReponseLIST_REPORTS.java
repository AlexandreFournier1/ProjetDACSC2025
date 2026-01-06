package hepl.dacsc.lib.reponse;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Reponse;

import java.util.List;

public class ReponseLIST_REPORTS implements Reponse {
    private boolean success;
    private List<byte[]> encryptedReports;
    private byte[] hmac;

    public ReponseLIST_REPORTS(boolean success, List<byte[]> encryptedReports, byte[] hmac) {
        this.success = success;
        this.encryptedReports = encryptedReports;
        this.hmac = hmac;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<byte[]> getEncryptedReports() {
        return encryptedReports;
    }

    public byte[] getHmac() {
        return hmac;
    }
}
