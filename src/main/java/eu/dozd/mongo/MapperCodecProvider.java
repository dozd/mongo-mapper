package eu.dozd.mongo;

import eu.dozd.mongo.annotation.Embedded;
import eu.dozd.mongo.annotation.Entity;
import org.atteo.classindex.ClassIndex;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * Mongo codec provider for mapped entities. Should be passed to Mongo configuration with other providers.
 */
public class MapperCodecProvider implements CodecProvider {
    private Map<Class, EntityInfo> entityMap = new HashMap<>();

    public MapperCodecProvider() {
        // Standard mapped classes.
        for (Class<?> klass : ClassIndex.getAnnotated(Entity.class)) {
            EntityInfo info = new EntityInfoWithId(klass);
            entityMap.put(klass, info);
        }

        // Process embedded (without id) classes.
        for (Class<?> klass : ClassIndex.getAnnotated(Embedded.class)) {
            EntityInfo info = new EntityInfo(klass);
            entityMap.put(klass, info);
        }
    }

    @Override
    public <T> Codec<T> get(final Class<T> clazz, final CodecRegistry registry) {
        EntityInfo info = entityMap.get(clazz);

        // CodecProvider returns null if it's not a provider for the requresed Class.
        if (info == null) {
            return null;
        }

        // Create codec for given class.
        return new EntityCodec<>(clazz, info);
    }
}
