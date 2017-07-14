package eu.dozd.mongo;

import eu.dozd.mongo.annotation.Embedded;
import eu.dozd.mongo.annotation.Entity;
import eu.dozd.mongo.annotation.Id;
import org.atteo.classindex.ClassIndex;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.data.mongodb.core.mapping.Document;

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
            EntityInfo info = new EntityInfoWithId(klass, Id.class);
            entityMap.put(klass, info);
        }

        // Process embedded (without id) classes.
        for (Class<?> klass : ClassIndex.getAnnotated(Embedded.class)) {
            EntityInfo info = new EntityInfo(klass);
            entityMap.put(klass, info);
        }

        boolean springOnClasspath;
        try {
            Class.forName("org.springframework.data.mongodb.core.mapping.Document", false, this.getClass().getClassLoader());
            Class.forName("org.springframework.data.annotation.Id", false, this.getClass().getClassLoader());
            springOnClasspath = true;
        } catch (ClassNotFoundException e) {
            springOnClasspath = false;
        }

        if (springOnClasspath) {
            // Spring mapped classes.
            for (Class<?> klass : ClassIndex.getAnnotated(Document.class)) {
                EntityInfo info = new EntityInfoWithId(klass, org.springframework.data.annotation.Id.class);
                entityMap.put(klass, info);
            }
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
