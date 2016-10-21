package eu.dozd.mongo;

import eu.dozd.mongo.codec.CustomFieldCodecProvider;
import org.bson.codecs.configuration.CodecProvider;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class MongoMapperTest {
    @Test
    public void testMongoMapper() throws Exception {
        List<CodecProvider> providers = MongoMapper.getProviders();
        Assert.assertFalse(providers.isEmpty());

        int size = providers.size();
        // Check for unmodifiable list.
        providers.add(new CustomFieldCodecProvider());

        MongoMapper.addProvider(new CustomFieldCodecProvider());
        providers = MongoMapper.getProviders();
        Assert.assertEquals(size + 1, providers.size());
    }
}
