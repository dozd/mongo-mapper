package eu.dozd.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import eu.dozd.mongo.entity.*;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

public class MongoMapperIT {

    private MongoDatabase db;
    private String dbName;
    private MongoClient client;

    @Before
    public void setUp() throws Exception {
        CodecRegistry codecRegistry = CodecRegistries.fromProviders(MongoMapper.getProviders());
        MongoClientOptions settings = MongoClientOptions.builder().codecRegistry(codecRegistry).build();

        String port = System.getProperty("embedMongoPort");
        Assert.assertNotNull(port);

        client = new MongoClient(new ServerAddress("127.0.0.1", Integer.parseInt(port)), settings);
        dbName = "mapper_test" + UUID.randomUUID();
        db = client.getDatabase(dbName);
    }

    @After
    public void tearDown() throws Exception {
        if (client != null) {
            client.dropDatabase(dbName);
            client.close();
        }
    }

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
}
