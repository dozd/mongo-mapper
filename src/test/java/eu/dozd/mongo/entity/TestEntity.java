package eu.dozd.mongo.entity;

import eu.dozd.mongo.annotation.Entity;
import eu.dozd.mongo.annotation.Id;

@Entity
public class TestEntity {

    @Id
    private String id;
    private int i;
    private boolean checked;
    private String name;
    private Integer j;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getJ() {
        return j;
    }

    public void setJ(Integer j) {
        this.j = j;
    }
}
