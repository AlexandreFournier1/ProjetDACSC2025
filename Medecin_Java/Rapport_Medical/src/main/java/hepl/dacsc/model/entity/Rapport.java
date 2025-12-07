package hepl.dacsc.model.entity;

import java.time.LocalDate;

public class Rapport extends Entity {
    private Integer idDoctor;
    private Integer idPatient;
    private LocalDate date;
    private String textRapport;

    public Rapport() {}
    public Rapport(Integer id, Integer idDoctor, Integer idPatient, LocalDate date, String textRapport) {
        super(id);
        this.idDoctor = idDoctor;
        this.idPatient = idPatient;
        this.date = date;
        this.textRapport = textRapport;
    }

    public Integer getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(Integer idDoctor) {
        this.idDoctor = idDoctor;
    }

    public Integer getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(Integer idPatient) {
        this.idPatient = idPatient;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTextRapport() {
        return textRapport;
    }

    public void setTextRapport(String textRapport) {
        this.textRapport = textRapport;
    }
}
