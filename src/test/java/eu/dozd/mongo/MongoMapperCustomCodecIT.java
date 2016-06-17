package eu.dozd.mongo;

import com.mongodb.client.MongoCollection;
import eu.dozd.mongo.codec.CustomFieldCodecProvider;
import eu.dozd.mongo.entity.CustomCodecField;
import eu.dozd.mongo.entity.TestEntityCustomCodec;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MongoMapperCustomCodecIT extends AbstractMongoIT {
    @Before
    public void setUp() throws Exception {
        MongoMapper.addProvider(new CustomFieldCodecProvider());
        super.setUp();
    }


    @Test
    public void testCustomCodec() throws Exception {
        MongoCollection<TestEntityCustomCodec> collection = db.getCollection("test_customcodec", TestEntityCustomCodec.class);
        collection.drop();

        TestEntityCustomCodec entity = new TestEntityCustomCodec();
        entity.setCustomCodecField(new CustomCodecField("a/b"));
        Assert.assertEquals("a", entity.getCustomCodecField().getPart1());
        Assert.assertEquals("b", entity.getCustomCodecField().getPart2());

        collection.insertOne(entity);

        TestEntityCustomCodec returned = collection.find().first();
        Assert.assertEquals("a", returned.getCustomCodecField().getPart1());
        Assert.assertEquals("b", returned.getCustomCodecField().getPart2());
    }
}
