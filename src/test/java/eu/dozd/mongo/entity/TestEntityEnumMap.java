package eu.dozd.mongo.entity;

import eu.dozd.mongo.annotation.Entity;
import eu.dozd.mongo.annotation.Id;

import java.util.Map;

@Entity
public class TestEntityEnumMap {

    @Id
    private String id;

    public enum QualityOfLife {
        BEST,
        EVEN_BETTER;

        @Override
        public String toString() {
            return super.toString();
        }
    }

    private Map<QualityOfLife, Number> qualityOfLifeMap;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setQualityOfLifeMap(Map<QualityOfLife, Number> qualityOfLifeMap) {
        this.qualityOfLifeMap = qualityOfLifeMap;
    }

    public Map<QualityOfLife, Number> getQualityOfLifeMap() {
        return qualityOfLifeMap;
    }
}
