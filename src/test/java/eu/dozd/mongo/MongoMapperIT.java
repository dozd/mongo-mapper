package eu.dozd.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
        client.dropDatabase(dbName);
        client.close();
    }

    @Test
    public void testBasicMapping() {
        MongoCollection<TestEntity> collection = db.getCollection("test", TestEntity.class);
        collection.drop();

        TestEntity entity = new TestEntity();
        entity.setChecked(true);
        entity.setName("name");
        entity.setI(2);

        collection.insertOne(entity);

        TestEntity returned = collection.find().first();
        Assert.assertEquals(entity.isChecked(), returned.isChecked());
        Assert.assertEquals(entity.getName(), returned.getName());
        Assert.assertEquals(entity.getI(), returned.getI());
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
}
