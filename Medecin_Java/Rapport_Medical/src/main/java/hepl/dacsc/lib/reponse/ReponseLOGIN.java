package hepl.dacsc.lib.reponse;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Reponse;

import java.util.Date;

// Réponse envoyée par le serveur
public class ReponseLOGIN implements Reponse {
    private boolean success;
    private String salt;
    private long temps;
    private double alea;

    public ReponseLOGIN(boolean success, String message) {
        this.success = success;
        // Construction du sel
        this.temps = new Date().getTime();
        this.alea  = Math.random();

        salt = String.valueOf(temps + alea);
    }

    public boolean isSuccess() { return success; }
    public String getSalt() { return salt; }
}