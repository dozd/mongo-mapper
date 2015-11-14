package eu.dozd.mongo;

import org.bson.codecs.BsonValueCodecProvider;
import org.bson.codecs.DocumentCodecProvider;
import org.bson.codecs.ValueCodecProvider;
import org.bson.codecs.configuration.CodecProvider;

import java.util.Arrays;
import java.util.List;

/**
 * Usage:
 *
 * CodecRegistry codecRegistry = CodecRegistries.fromProviders(MongoMapper.getProviders());
 *
 * MongoClientSettings settings = MongoClientSettings.builder().codecRegistry(codecRegistry).build();
 * MongoClient mongoClient = MongoClients.create(settings);
 *
 * If you want modify or add other providers, instantiate MapperCodecProvider directly.
 */
public class MongoMapper {
    public static List<CodecProvider> getProviders() {
        return Arrays.asList(new ValueCodecProvider(),
                new DocumentCodecProvider(),
                new BsonValueCodecProvider(),
                new MapperCodecProvider());

    }
}
