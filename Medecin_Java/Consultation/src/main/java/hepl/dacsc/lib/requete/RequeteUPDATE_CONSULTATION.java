package hepl.dacsc.lib.requete;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Requete;
import hepl.dacsc.model.entity.Patient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class RequeteUPDATE_CONSULTATION implements Requete {
    private int id;
    private LocalDate newDate;
    private LocalTime newHour;
    private Patient patient;
    private String reason;

    public RequeteUPDATE_CONSULTATION(int id, LocalDate newDate, LocalTime newHour, Patient patient, String reason) {
        this.id = id;
        this.newDate = newDate;
        this.newHour = newHour;
        this.patient = patient;
        this.reason = reason;
    }

    public int getId() {
        return id;
    }

    public LocalDate getNewDate() {
        return newDate;
    }

    public LocalTime getNewHour() {
        return newHour;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getReason() {
        return reason;
    }
}
