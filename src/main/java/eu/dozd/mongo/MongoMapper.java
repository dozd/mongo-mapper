package eu.dozd.mongo;

import eu.dozd.mongo.codecs.bigdecimal.BigDecimalCodecProvider;
import org.bson.codecs.BsonValueCodecProvider;
import org.bson.codecs.DocumentCodecProvider;
import org.bson.codecs.ValueCodecProvider;
import org.bson.codecs.configuration.CodecProvider;

import java.util.ArrayList;
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
    private final static ArrayList<CodecProvider> providers = new ArrayList<>(Arrays.asList(
            new ValueCodecProvider(),
            new DocumentCodecProvider(),
            new BsonValueCodecProvider(),
            new MapperCodecProvider(),
            new BigDecimalCodecProvider()
    ));

    public static List<CodecProvider> getProviders() {
        return (ArrayList) providers.clone();
    }

    public static void addProvider(CodecProvider provider) {
        providers.add(provider);
    }
}
