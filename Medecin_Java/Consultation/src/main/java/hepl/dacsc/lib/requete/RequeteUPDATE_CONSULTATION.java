package hepl.dacsc.lib.requete;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Requete;
import hepl.dacsc.model.entity.Patient;

import java.util.Date;

public class RequeteUPDATE_CONSULTATION implements Requete {
    private int id;
    private Date newDate;
    private String newHour;
    private Patient patient;
    private String reason;

    public RequeteUPDATE_CONSULTATION(int id, Date newDate, String newHour, Patient patient, String reason) {
        this.id = id;
        this.newDate = newDate;
        this.newHour = newHour;
        this.patient = patient;
        this.reason = reason;
    }

    public int getId() {
        return id;
    }

    public Date getNewDate() {
        return newDate;
    }

    public String getNewHour() {
        return newHour;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getReason() {
        return reason;
    }
}
