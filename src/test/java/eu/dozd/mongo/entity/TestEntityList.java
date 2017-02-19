package eu.dozd.mongo.entity;

import eu.dozd.mongo.annotation.Entity;
import eu.dozd.mongo.annotation.Id;

import java.util.List;

@Entity
public class TestEntityList {
    @Id
    private String id;
    private boolean checked;
    private String name;
    private Integer j;
    private List<TestEntityEmbedded> list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<TestEntityEmbedded> getList() {
        return list;
    }

    public void setList(List<TestEntityEmbedded> list) {
        this.list = list;
    }
}
