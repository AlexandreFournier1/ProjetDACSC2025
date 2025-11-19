package hepl.dacsc.model.viewmodel;

public class DoctorSearchVM {
    private Integer id;
    private Integer speciality_id;
    private String last_name;
    private String first_name;
    private String mdp;

    public DoctorSearchVM() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
    public String getMdp() {
        return mdp;
    }
    public void setMdp(String mdp) {
        this.mdp = mdp;
    }
}
