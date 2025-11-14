package hepl.dacsc.model.entity;

public class Doctor extends Entity {
    private Integer speciality_id;
    private String last_name;
    private String first_name;

    public Doctor() {}
    public Doctor(int id, Integer speciality_id, String last_name, String first_name) {
        super(id);
        this.speciality_id = speciality_id;
        this.last_name = last_name;
        this.first_name = first_name;
    }
    public Integer getSpeciality_id() {
        return speciality_id;
    }

    public void setSpeciality_id(Integer speciality_id) {
        this.speciality_id = speciality_id;
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

}
