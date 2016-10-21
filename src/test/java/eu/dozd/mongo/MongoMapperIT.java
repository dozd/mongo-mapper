package eu.dozd.mongo;

import com.mongodb.client.MongoCollection;
import eu.dozd.mongo.entity.*;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;

public class MongoMapperIT extends AbstractMongoIT {
    @Test
    public void testBasicMapping() {
        MongoCollection<TestEntity> collection = db.getCollection("test", TestEntity.class);
        collection.drop();

        TestEntity entity = new TestEntity();
        entity.setChecked(true);
        entity.setName("name");
        entity.setI(2);
        entity.setJ(1);

        collection.insertOne(entity);

        TestEntity returned = collection.find().first();
        Assert.assertEquals(entity.isChecked(), returned.isChecked());
        Assert.assertEquals(entity.getName(), returned.getName());
        Assert.assertEquals(entity.getI(), returned.getI());
        Assert.assertEquals(entity.getJ(), returned.getJ());
    }

    @Test
    public void testReferenceMapping() {
        MongoCollection<TestEntityRef> collection = db.getCollection("test_ref", TestEntityRef.class);
        collection.drop();

        TestEntityRef entityRef = new TestEntityRef();
        entityRef.setName("ref");

        TestEntity entity = new TestEntity();
        entity.setName("bla");
        entityRef.setTestEntity(entity);

        collection.insertOne(entityRef);

        TestEntityRef returned = collection.find().first();
        Assert.assertEquals(entityRef.getName(), returned.getName());
        Assert.assertEquals(entityRef.getTestEntity().getName(), returned.getTestEntity().getName());
        Assert.assertEquals(entityRef.getTestEntity().isChecked(), returned.getTestEntity().isChecked());
        Assert.assertEquals(entityRef.getTestEntity().getI(), returned.getTestEntity().getI());
    }

    @Test
    public void testEnum() {
        MongoCollection<TestEntityEnum> collection = db.getCollection("test_enum", TestEntityEnum.class);
        collection.drop();

        TestEntityEnum entityEnum = new TestEntityEnum();
        entityEnum.setType(TestEntityEnum.Type.HIGH);

        collection.insertOne(entityEnum);

        TestEntityEnum returned = collection.find().first();
        Assert.assertEquals(entityEnum.getType(), returned.getType());
    }

    @Test
    public void testNullEnum() throws Exception {
        MongoCollection<TestEntityEnum> collection = db.getCollection("test_enum2", TestEntityEnum.class);
        collection.drop();

        TestEntityEnum entityEnum = new TestEntityEnum();
        entityEnum.setType(null);

        collection.insertOne(entityEnum);

        TestEntityEnum returned = collection.find().first();
        Assert.assertNull(returned.getType());
    }

    @Test
    public void testOtherCodec() {
        MongoCollection<TestEntityBigDecimal> collection = db.getCollection("test_bigdecimal", TestEntityBigDecimal.class);
        collection.drop();

        TestEntityBigDecimal entity = new TestEntityBigDecimal();
        entity.setBigNumber(BigDecimal.valueOf(1234124L, 3));

        collection.insertOne(entity);

        TestEntityBigDecimal returned = collection.find().first();
        Assert.assertEquals(entity.getBigNumber(), returned.getBigNumber());
    }

    @Test
    public void testNull() {
        MongoCollection<TestEntity> collection = db.getCollection("test_null", TestEntity.class);
        collection.drop();

        TestEntity entity = new TestEntity();
        entity.setChecked(true);
        entity.setName(null);
        entity.setI(2);
        entity.setJ(null);

        collection.insertOne(entity);

        TestEntity returned = collection.find().first();
        Assert.assertEquals(entity.isChecked(), returned.isChecked());
        Assert.assertNull(returned.getName());
        Assert.assertEquals(entity.getI(), returned.getI());
        Assert.assertNull(returned.getJ());
    }

    @Test
    public void testEmbedded() throws Exception {
        MongoCollection<TestEntityWithEmbedded> collection = db.getCollection("test_embedded", TestEntityWithEmbedded.class);
        collection.drop();

        TestEntityEmbedded embedded = new TestEntityEmbedded();
        embedded.setAge(1);
        embedded.setName("testing");

        TestEntityWithEmbedded entity = new TestEntityWithEmbedded();
        entity.setName("embedded");
        entity.setEmbedded(embedded);

        collection.insertOne(entity);

        TestEntityWithEmbedded returned = collection.find().first();
        Assert.assertEquals(entity.getEmbedded(), returned.getEmbedded());
        Assert.assertEquals(entity.getName(), returned.getName());
    }

    @Test
    public void testMoreFields() throws Exception {
        MongoCollection<TestEntity> collection = db.getCollection("test_morefields", TestEntity.class);
        collection.drop();

        TestEntity entity = new TestEntity();
        entity.setName("a");
        entity.setChecked(true);
        entity.setJ(2);
        collection.insertOne(entity);

        MongoCollection<TestEntityRef> collection2 = db.getCollection("test_morefields", TestEntityRef.class);
        TestEntityRef returned = collection2.find().first();

        Assert.assertEquals(entity.getName(), returned.getName());
        Assert.assertEquals(entity.getId(), returned.getId());
    }

    @Test
    public void testDoubleList() throws Exception {
        MongoCollection<TestEntityDoubleList> collection = db.getCollection("test_embedded", TestEntityDoubleList.class);
        collection.drop();

        TestEntityDoubleList doubleList = new TestEntityDoubleList();
        doubleList.setDoubleList(Arrays.asList(0.0, 6.5));

        TestEntityDoubleList entity = new TestEntityDoubleList();
        entity.setDoubleList(Arrays.asList(0.0, 6.5));

        collection.insertOne(entity);

        TestEntityDoubleList returned = collection.find().first();
        Assert.assertEquals(entity.getDoubleList(), returned.getDoubleList());
    }
}
