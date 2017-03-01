package eu.dozd.mongo.entity;

import eu.dozd.mongo.annotation.Entity;
import eu.dozd.mongo.annotation.Id;
import org.bson.types.ObjectId;

@Entity
public class TestEntityObjectId {
    @Id
    private ObjectId id;
    private String name;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
