package eu.dozd.mongo.entity;

import eu.dozd.mongo.annotation.Entity;
import eu.dozd.mongo.annotation.Id;

@Entity
public class TestEntityWithEmbedded {
    @Id
    private String id;
    private String name;
    private TestEntityEmbedded embedded;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TestEntityEmbedded getEmbedded() {
        return embedded;
    }

    public void setEmbedded(TestEntityEmbedded embedded) {
        this.embedded = embedded;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
