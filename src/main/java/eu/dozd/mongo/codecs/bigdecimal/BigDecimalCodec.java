package eu.dozd.mongo.codecs.bigdecimal;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.math.BigDecimal;

public class BigDecimalCodec implements Codec<BigDecimal> {
    @Override
    public BigDecimal decode(final BsonReader reader, final DecoderContext decoderContext) {
        return new BigDecimal(reader.readString());
    }

    @Override
    public void encode(BsonWriter bsonWriter, BigDecimal bigDecimal, EncoderContext encoderContext) {
        bsonWriter.writeString(bigDecimal.toPlainString());
    }

    @Override
    public Class<BigDecimal> getEncoderClass() {
        return BigDecimal.class;
    }
}