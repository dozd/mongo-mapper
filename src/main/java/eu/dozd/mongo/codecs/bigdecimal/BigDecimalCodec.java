package eu.dozd.mongo.codecs.bigdecimal;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BigDecimalCodec implements Codec<BigDecimal> {
    @Override
    public BigDecimal decode(final BsonReader reader, final DecoderContext decoderContext) {
        reader.readStartArray();
        long unscaled = reader.readInt64();
        int scale = reader.readInt32();
        reader.readEndArray();
        BigDecimal bigDecimal = new BigDecimal(new BigInteger(String.valueOf(unscaled)), scale);
        return bigDecimal;
    }

    @Override
    public void encode(BsonWriter bsonWriter, BigDecimal bigDecimal, EncoderContext encoderContext) {
        bsonWriter.writeStartArray();
        bsonWriter.writeInt64(bigDecimal.unscaledValue().longValue());
        bsonWriter.writeInt32(bigDecimal.scale());
        bsonWriter.writeEndArray();
    }

    @Override
    public Class<BigDecimal> getEncoderClass() {
        return BigDecimal.class;
    }
}