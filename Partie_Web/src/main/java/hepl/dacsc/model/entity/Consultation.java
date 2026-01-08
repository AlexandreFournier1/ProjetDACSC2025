package hepl.dacsc.model.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Consultation extends Entity implements Serializable {
    private Integer doctor_id;
    private String doctor_name;
    private String speciality_name;
    private Integer patient_id;
    private LocalDate date;
    private LocalTime hour;
    private Integer duree;
    private String reason;

    public Consultation() {}

    public Consultation(Integer id, Integer doctor_id, String doctor_name, String speciality_name, Integer patient_id, LocalDate date, LocalTime hour, Integer duree,String reason) {
        super(id);
        this.doctor_id = doctor_id;
        this.doctor_name = doctor_name;
        this.speciality_name = speciality_name;
        this.patient_id = patient_id;
        this.date = date;
        this.hour = hour;
        this.duree = duree;
        this.reason = reason;
    }
    public Integer getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(Integer doctor_id) {
        this.doctor_id = doctor_id;
    }

    public Integer getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(Integer patient_id) {this.patient_id = patient_id;}

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getHour() {
        return hour;
    }

    public void setHour(LocalTime hour) {
        this.hour = hour;
    }

    public Integer getDuree() {
        return duree;
    }

    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getSpeciality_name() {
        return speciality_name;
    }

    public void setSpeciality_name(String speciality_name) {
        this.speciality_name = speciality_name;
    }
}
