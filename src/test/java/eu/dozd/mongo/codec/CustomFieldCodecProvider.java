package eu.dozd.mongo.codec;

import eu.dozd.mongo.entity.CustomCodecField;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class CustomFieldCodecProvider implements CodecProvider {
    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry codecRegistry) {
        if (clazz == CustomCodecField.class) {
            return (Codec<T>) new CustomFieldCodec();
        }

        return null;
    }

}
