package hepl.dacsc.model.entity;

import java.time.LocalDate;

public class Patient extends Entity {
    private String last_name;
    private String first_name;
    private LocalDate birth_date;

    public Patient() {}
    public Patient(Integer id, String last_name, String first_name, LocalDate birth_date) {
        super(id);
        this.last_name = last_name;
        this.first_name = first_name;
        this.birth_date = birth_date;
    }
    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public LocalDate getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(LocalDate birth_date) {
        this.birth_date = birth_date;
    }
}
