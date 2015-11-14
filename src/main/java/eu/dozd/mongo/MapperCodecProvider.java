package eu.dozd.mongo;

import eu.dozd.mongo.annotation.Entity;
import eu.dozd.mongo.annotation.Id;
import org.atteo.classindex.ClassIndex;
import org.bson.codecs.Codec;
import org.bson.codecs.ValueCodecProvider;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Mongo codec provider for mapped entites. Should be passed to Mongo configuration with other providers.
 */
public class MapperCodecProvider implements CodecProvider {
    private Map<Class, EntityInfo> entityMap = new HashMap<>();

    public MapperCodecProvider() {
        for (Class<?> klass : ClassIndex.getAnnotated(Entity.class)) {
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
