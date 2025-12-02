package hepl.dacsc.ServerGeneriqueTCP.interfaces;

import java.io.Serializable;

public class Requete implements Serializable {
    private byte[] data;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
