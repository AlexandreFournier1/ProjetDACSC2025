package model.entity;

public class Speciality extends Entity {
    private String name;

    public Speciality() {}
    public Speciality(int id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
