package eu.dozd.mongo;

import eu.dozd.mongo.entity.TestEntity;
import org.bson.codecs.Codec;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MapperCodecProviderTest {

    private MapperCodecProvider provider;

    @Before
    public void setUp() throws Exception {
        provider = new MapperCodecProvider();
    }

    @Test
    public void testGetCodec() {
        Codec<TestEntity> codec = provider.get(TestEntity.class, null);
        Assert.assertNotNull(codec);
    }

    @Test
    public void testGetCodecNotFound() {
        Assert.assertNull(provider.get(this.getClass(), null));
    }
}