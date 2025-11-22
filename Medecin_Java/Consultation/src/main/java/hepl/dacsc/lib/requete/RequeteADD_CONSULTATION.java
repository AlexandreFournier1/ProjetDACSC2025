package hepl.dacsc.lib.requete;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Requete;

import java.time.LocalDate;
import java.time.LocalTime;

public class RequeteADD_CONSULTATION implements Requete {
    private int idDoctor;
    private LocalDate date;
    private LocalTime hour;
    private int duration;
    private int nbConsultation;

    public RequeteADD_CONSULTATION(int idDoctor, LocalDate date, LocalTime hour, int duration, int nbConsultation) {
        this.idDoctor = idDoctor;
        this.date = date;
        this.hour = hour;
        this.duration = duration;
        this.nbConsultation = nbConsultation;
    }

    public int getIdDoctor() {
        return idDoctor;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getHour() {
        return hour;
    }

    public int getDuration() {
        return duration;
    }

    public int getNbConsultation() {
        return nbConsultation;
    }
}
