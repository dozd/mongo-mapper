package eu.dozd.mongo.entity;

import eu.dozd.mongo.annotation.Entity;
import eu.dozd.mongo.annotation.Id;

@Entity
public class TestEntityRef {
    @Id
    private String id;
    private String name;

    private TestEntity testEntity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TestEntity getTestEntity() {
        return testEntity;
    }

    public void setTestEntity(TestEntity testEntity) {
        this.testEntity = testEntity;
    }
}
