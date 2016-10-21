package eu.dozd.mongo.entity;

import eu.dozd.mongo.annotation.Entity;
import eu.dozd.mongo.annotation.Id;

import java.beans.Transient;

@Entity
public class TestEntityTransient {

    @Id
    private String id;

    private String nope;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Transient
    public String getNope() {
        return nope;
    }

    public void setNope(String nope) {
        this.nope = nope;
    }
}
