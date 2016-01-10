package eu.dozd.mongo;

import eu.dozd.mongo.annotation.Entity;
import eu.dozd.mongo.annotation.Id;

@Entity
public class TestEntityEnum {
    @Id
    private String id;

    private Type type;

    public enum Type {
        HIGH
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
