package hepl.dacsc.lib.reponse;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Reponse;

public class ReponseLOGIN_DIGEST implements Reponse {
    private boolean success;
    private byte[] encryptedSessionKey;

    public ReponseLOGIN_DIGEST(boolean success, byte[] encryptedSessionKey) {
        this.success = success;
        this.encryptedSessionKey = encryptedSessionKey;
    }

    public boolean isSuccess() {
        return success;
    }

    public byte[] getEncryptedSessionKey() {
        return encryptedSessionKey;
    }
}
