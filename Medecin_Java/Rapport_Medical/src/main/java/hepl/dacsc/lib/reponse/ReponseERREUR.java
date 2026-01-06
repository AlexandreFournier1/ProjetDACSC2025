package hepl.dacsc.lib.reponse;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Reponse;

public class ReponseERREUR implements Reponse {
    private final boolean success;
    private final String message;
    private final int code;

    // Erreur simple
    public ReponseERREUR(String message) {
        this.success = false;
        this.message = message;
        this.code = -1;
    }

    // Erreur avec code
    public ReponseERREUR(int code, String message) {
        this.success = false;
        this.message = message;
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}