package eu.dozd.mongo.codec;

import eu.dozd.mongo.entity.CustomCodecField;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class CustomFieldCodec implements Codec<CustomCodecField> {
    @Override
    public CustomCodecField decode(BsonReader reader, DecoderContext decoderContext) {
        String s = reader.readString();
        return new CustomCodecField(s);
    }

    @Override
    public void encode(BsonWriter writer, CustomCodecField value, EncoderContext encoderContext) {
        writer.writeString(value.toString());
    }

    @Override
    public Class<CustomCodecField> getEncoderClass() {
        return CustomCodecField.class;
    }
}
