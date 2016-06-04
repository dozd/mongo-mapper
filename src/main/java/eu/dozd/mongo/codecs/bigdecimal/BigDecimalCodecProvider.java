package eu.dozd.mongo.codecs.bigdecimal;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

import java.math.BigDecimal;

public class BigDecimalCodecProvider implements CodecProvider {
    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry codecRegistry) {
        if (clazz == BigDecimal.class) {
            return (Codec<T>) new BigDecimalCodec();
        }

        return null;
    }
}
