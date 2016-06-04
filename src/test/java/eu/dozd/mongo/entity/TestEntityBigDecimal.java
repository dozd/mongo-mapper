package eu.dozd.mongo.entity;

import eu.dozd.mongo.annotation.Entity;
import eu.dozd.mongo.annotation.Id;

import java.math.BigDecimal;

@Entity
public class TestEntityBigDecimal {
    @Id
    private String id;

    private BigDecimal bigNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getBigNumber() {
        return bigNumber;
    }

    public void setBigNumber(BigDecimal bigNumber) {
        this.bigNumber = bigNumber;
    }
}
