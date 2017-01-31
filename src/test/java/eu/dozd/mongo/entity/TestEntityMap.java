package eu.dozd.mongo.entity;

import eu.dozd.mongo.annotation.Entity;
import eu.dozd.mongo.annotation.Id;

import java.util.Map;

@Entity
public class TestEntityMap {
    @Id
    private String id;

    private Map<String, TestEntityEmbedded> childs;
    private Map<String, Boolean> bools;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, TestEntityEmbedded> getChilds() {
        return childs;
    }

    public void setChilds(Map<String, TestEntityEmbedded> childs) {
        this.childs = childs;
    }

    public Map<String, Boolean> getBools() {
        return bools;
    }

    public void setBools(Map<String, Boolean> bools) {
        this.bools = bools;
    }
}
