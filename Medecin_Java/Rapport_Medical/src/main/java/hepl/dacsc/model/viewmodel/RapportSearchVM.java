package hepl.dacsc.model.viewmodel;

import java.time.LocalDate;

public class RapportSearchVM {
    private Integer id;
    private Integer idDoctor;
    private Integer idPatient;
    private LocalDate date;
    private String textRapport;
    public RapportSearchVM() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
