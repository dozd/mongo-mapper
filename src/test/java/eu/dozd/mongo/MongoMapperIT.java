package eu.dozd.mongo;

import com.mongodb.client.MongoCollection;
import eu.dozd.mongo.entity.*;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static eu.dozd.mongo.entity.TestEntityEnumMap.QualityOfLife.BEST;
import static eu.dozd.mongo.entity.TestEntityEnumMap.QualityOfLife.EVEN_BETTER;

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
        entity.setBigNumber(new BigDecimal("0.0100000000000000000000000001"));

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
    public void testTransient() {
        MongoCollection<TestEntityTransient> collection = db.getCollection("test_array", TestEntityTransient.class);
        collection.drop();

        TestEntityTransient entity = new TestEntityTransient();
        entity.setNope("ahoj");

        collection.insertOne(entity);

        TestEntityTransient returned = collection.find().first();
        Assert.assertNull(returned.getNope());

    }

    @Test
    public void testDoubleList() throws Exception {
        MongoCollection<TestEntityDoubleList> collection = db.getCollection("test_doublelist", TestEntityDoubleList.class);
        collection.drop();

        TestEntityDoubleList entity = new TestEntityDoubleList();
        entity.setDoubleList(Arrays.asList(0.0, 6.5));

        collection.insertOne(entity);

        TestEntityDoubleList returned = collection.find().first();
        Assert.assertEquals(entity.getDoubleList(), returned.getDoubleList());
    }

    @Test
    @Ignore("Ignored for now because there is no quick fix.")
    public void testEnumMap() throws Exception {
        MongoCollection<TestEntityEnumMap> collection = db.getCollection("test_embedded", TestEntityEnumMap.class);
        collection.drop();

        TestEntityEnumMap enumMapEntity = new TestEntityEnumMap();
        final Map<TestEntityEnumMap.QualityOfLife, Number> enumMap = new HashMap<>();
        enumMap.put(BEST, 5);
        enumMap.put(EVEN_BETTER, 10);
        enumMapEntity.setQualityOfLifeMap(enumMap);


        TestEntityEnumMap entity = new TestEntityEnumMap();
        final Map<TestEntityEnumMap.QualityOfLife, Number> expectedEnumMap = new HashMap<>();
        expectedEnumMap.put(BEST, 5);
        expectedEnumMap.put(EVEN_BETTER, 10);
        entity.setQualityOfLifeMap(expectedEnumMap);

        collection.insertOne(entity);

        TestEntityEnumMap returned = collection.find().first();
        Assert.assertEquals(entity.getQualityOfLifeMap(), returned.getQualityOfLifeMap());
    }

    @Test
    public void testReferenceMappingNull() {
        MongoCollection<TestEntityRef> collection = db.getCollection("test_ref", TestEntityRef.class);
        collection.drop();

        TestEntityRef entityRef = new TestEntityRef();
        entityRef.setName("ref");
        entityRef.setTestEntity(null);

        collection.insertOne(entityRef);

        TestEntityRef returned = collection.find().first();
        Assert.assertEquals(entityRef.getName(), returned.getName());
        Assert.assertNull(entityRef.getTestEntity());
    }

    @Test
    public void testMapEntityChild() {
        MongoCollection<TestEntityMap> collection = db.getCollection("test_map", TestEntityMap.class);
        collection.drop();

        Map<String, TestEntityEmbedded> childs = new HashMap<>();
        TestEntityEmbedded e1 = new TestEntityEmbedded();
        e1.setAge(1);
        e1.setName("child1");
        childs.put("c1", e1);

        TestEntityEmbedded e2 = new TestEntityEmbedded();
        e2.setAge(2);
        e2.setName("child2");
        childs.put("c2", e2);

        Map<String, Boolean> bools = new HashMap<>();
        bools.put("b1", true);
        bools.put("b2", false);

        TestEntityMap entityRef = new TestEntityMap();
        entityRef.setChilds(childs);
        entityRef.setBools(bools);

        collection.insertOne(entityRef);

        TestEntityMap returned = collection.find().first();
        Assert.assertEquals(childs.size(), returned.getChilds().size());
        Assert.assertTrue(returned.getChilds().containsKey("c1"));
        Assert.assertTrue(returned.getChilds().containsKey("c2"));
        Assert.assertEquals(childs.get("c1").getAge(), returned.getChilds().get("c1").getAge());
        Assert.assertEquals(childs.get("c1").getName(), returned.getChilds().get("c1").getName());
        Assert.assertEquals(childs.get("c2").getAge(), returned.getChilds().get("c2").getAge());
        Assert.assertEquals(childs.get("c2").getName(), returned.getChilds().get("c2").getName());

        Assert.assertEquals(bools.size(), returned.getBools().size());
        Assert.assertTrue(returned.getBools().containsKey("b1"));
        Assert.assertTrue(returned.getBools().containsKey("b2"));
        Assert.assertEquals(bools.get("b1"), returned.getBools().get("b1"));
    }

    @Test
    public void testGenericList() {
        MongoCollection<TestEntityList> collection = db.getCollection("test_list", TestEntityList.class);
        collection.drop();

        TestEntityEmbedded embeddedEntity1 = new TestEntityEmbedded();
        embeddedEntity1.setName("Entity 1");
        embeddedEntity1.setAge(1);

        TestEntityEmbedded embeddedEntity2 = new TestEntityEmbedded();
        embeddedEntity2.setName("Entity 2");
        embeddedEntity2.setAge(2);

        ArrayList<TestEntityEmbedded> list = new ArrayList<>();
        list.add(embeddedEntity1);
        list.add(embeddedEntity2);

        TestEntityList entity = new TestEntityList();
        entity.setList(list);

        collection.insertOne(entity);

        TestEntityList returned = collection.find().first();
        Assert.assertEquals(entity.getList().size(), entity.getList().size());
        Assert.assertEquals(entity.getList().get(0), returned.getList().get(0));
        Assert.assertEquals(entity.getList().get(1), returned.getList().get(1));
    }
}
