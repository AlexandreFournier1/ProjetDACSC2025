package hepl.dacsc.model.entity;

import java.io.Serializable;

public abstract class Entity implements Serializable {
    private Integer id;
    public Entity() {}
    public Entity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
