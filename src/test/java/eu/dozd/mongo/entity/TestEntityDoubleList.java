package eu.dozd.mongo.entity;

import eu.dozd.mongo.annotation.Entity;
import eu.dozd.mongo.annotation.Id;

import java.util.List;

@Entity
public class TestEntityDoubleList {
    @Id
    private String id;

    private List<Double> doubleList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Double> getDoubleList() {
        return doubleList;
    }

    public void setDoubleList(List<Double> doubleList) {
        this.doubleList = doubleList;
    }
}
