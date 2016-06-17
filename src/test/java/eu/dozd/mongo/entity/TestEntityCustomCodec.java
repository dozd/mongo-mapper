package eu.dozd.mongo.entity;

import eu.dozd.mongo.annotation.Entity;
import eu.dozd.mongo.annotation.Id;

@Entity
public class TestEntityCustomCodec {
    @Id
    private String id;

    private CustomCodecField customCodecField;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CustomCodecField getCustomCodecField() {
        return customCodecField;
    }

    public void setCustomCodecField(CustomCodecField customCodecField) {
        this.customCodecField = customCodecField;
    }
}
