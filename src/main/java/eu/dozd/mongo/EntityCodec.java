package eu.dozd.mongo;

import org.bson.*;
import org.bson.assertions.Assertions;
import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecConfigurationException;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Codec used to decode and encode registered entities.
 */
class EntityCodec<T> implements CollectibleCodec<T> {
    private static final String ID_FIELD = "_id";
    private final Class<T> clazz;
    private final EntityInfo info;
    private final IdGenerator idGenerator;
    private final DocumentCodec documentCodec;
    private final BsonTypeClassMap bsonTypeClassMap;
    private final CodecRegistry registry;

    public EntityCodec(Class<T> clazz, EntityInfo info) {
        this.clazz = clazz;
        this.info = info;
        idGenerator = Assertions.notNull("idGenerator", new ObjectIdGenerator());
        registry = CodecRegistries.fromProviders(MongoMapper.getProviders());
        documentCodec = new DocumentCodec(registry, new BsonTypeClassMap());
        bsonTypeClassMap = new BsonTypeClassMap();
    }

    @Override
    public T generateIdIfAbsentFromDocument(T t) {
        if (!documentHasId(t)) {
            info.setId(t, idGenerator.generate());
        }
        return t;
    }

    @Override
    public boolean documentHasId(T t) {
        if (info.getIdField() != null) {
            Object id = info.getId(t);
            return (id != null);
        }
        return false;
    }

    @Override
    public BsonValue getDocumentId(T t) {
        Object id = info.getId(t);
        ObjectId documentId;
        if (id instanceof ObjectId) {
            documentId = (ObjectId) id;
        } else if (id instanceof String) {
            documentId = new ObjectId((String) id);
        } else {
            throw new MongoMapperException("Id can be the type of ObjectId or String.");
        }
        return new BsonObjectId(documentId);
    }

    @Override
    public T decode(BsonReader bsonReader, DecoderContext decoderContext) {
        BsonType bsonType = bsonReader.getCurrentBsonType();

        if (bsonType == BsonType.NULL) {
            bsonReader.readNull();
            return null;
        }

        Document document = new Document();

        bsonReader.readStartDocument();

        while (bsonReader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String fieldName = bsonReader.readName();
            if (info.isMappedReference(fieldName)) {
                // Mapped reference to other entities should be decoded recursively.
                document.put(fieldName, registry.get(info.getFieldType(fieldName)).decode(bsonReader, decoderContext));
            } else if (info.isMap(fieldName)) {
                document.put(fieldName, decodeDocument(bsonReader, decoderContext, info.getMapValueType(fieldName)));
            } else if (info.isGenericList(fieldName)) {
                document.put(fieldName, readGenericList(bsonReader, decoderContext, info.getGenericListValueType(fieldName)));
            } else {
                document.put(fieldName, readValue(bsonReader, decoderContext, fieldName));
            }
        }

        bsonReader.readEndDocument();

        T t;
        try {
            t = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new MongoMapperException("Cannot create instance of class " + clazz.getName(), e);
        }

        for (String field : info.getFields()) {
            if (field.equals(info.getIdField())) {
                info.setId(t, document.get(ID_FIELD));
            } else {
                Object o;
                o = document.get(field);

                if (info.getFieldType(field).isEnum()) {
                    o = o == null ? null : Enum.valueOf((Class<? extends Enum>) info.getFieldType(field), (String) o);
                }
                info.setValue(t, field, o);
            }
        }

        return t;
    }

    private <V> List<V> readGenericList(BsonReader bsonReader, DecoderContext decoderContext, Class<V> valueClazz) {
        BsonType bsonType = bsonReader.getCurrentBsonType();

        if (bsonType == BsonType.NULL) {
            bsonReader.readNull();
            return null;
        }

        bsonReader.readStartArray();
        List<V> list = new ArrayList<>();
        while (bsonReader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            Codec<V> codec;
            try {
                codec = registry.get(valueClazz);
            } catch (CodecConfigurationException | MongoMapperException e) {
                // No other way to check without catching exception.
                codec = null;
            }

            V decode;
            if (codec != null) {
                decode = codec.decode(bsonReader, decoderContext);
            } else {
                decode = (V) readValue(bsonReader, decoderContext, null);
            }
            list.add(decode);
        }
        bsonReader.readEndArray();
        return list;
    }

    public <V> Document decodeDocument(BsonReader bsonReader, DecoderContext decoderContext, Class<V> valueClazz) {
        BsonType bsonType = bsonReader.getCurrentBsonType();

        if (bsonType == BsonType.NULL) {
            bsonReader.readNull();
            return null;
        }

        Document document = new Document();

        bsonReader.readStartDocument();
        while (bsonReader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String fieldName = bsonReader.readName();
            Codec<V> codec;
            try {
                codec = registry.get(valueClazz);
            } catch (CodecConfigurationException | MongoMapperException e) {
                // No other way to check without catching exception.
                codec = null;
            }

            V decode;
            if (codec != null) {
                decode = codec.decode(bsonReader, decoderContext);
            } else {
                decode = (V) readValue(bsonReader, decoderContext, null);
            }

            document.put(fieldName, decode);
        }

        bsonReader.readEndDocument();
        return document;
    }

    @Override
    public void encode(BsonWriter bsonWriter, T t, EncoderContext encoderContext) {
        Document document = new Document();

        for (String field : info.getFields()) {
            if (field.equals(info.getIdField())) {
                if (documentHasId(t)) {
                    document.put(ID_FIELD, info.getId(t));
                }
            } else if (info.getFieldType(field).isEnum()) {
                Enum anEnum = (Enum) info.getValue(t, field);
                String value = (anEnum == null) ? null : anEnum.name();
                document.put(field, value);
            } else {
                document.put(field, info.getValue(t, field));
            }
        }

        this.documentCodec.encode(bsonWriter, document, encoderContext);
    }

    @Override
    public Class<T> getEncoderClass() {
        return clazz;
    }

    private Object readValue(final BsonReader reader, final DecoderContext decoderContext, String fieldName) {
        BsonType bsonType = reader.getCurrentBsonType();

        if (bsonType == BsonType.NULL) {
            reader.readNull();
            return null;
        }

        Codec<?> codec = null;
        if (fieldName != null && !fieldName.equals(ID_FIELD)) {
            // Check whether there is special codec for given field.
            try {
                codec = registry.get(info.getFieldType(fieldName));
            } catch (CodecConfigurationException | MongoMapperException e) {
                // No other way to check without catching exception.
                codec = null;
            }

            if (codec != null) {
                return codec.decode(reader, decoderContext);
            }
        }

        // Fallback variant for other fields without codec from Document decoder.
        if (bsonType == BsonType.ARRAY) {
            return readList(reader, decoderContext);
        } else if (bsonType == BsonType.BINARY) {
            byte bsonSubType = reader.peekBinarySubType();
            if (bsonSubType == BsonBinarySubType.UUID_STANDARD.getValue() || bsonSubType == BsonBinarySubType.UUID_LEGACY.getValue()) {
                return registry.get(UUID.class).decode(reader, decoderContext);
            }
        }
        return registry.get(bsonTypeClassMap.get(bsonType)).decode(reader, decoderContext);
    }

    private List<Object> readList(final BsonReader reader, final DecoderContext decoderContext) {
        reader.readStartArray();
        List<Object> list = new ArrayList<>();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            list.add(readValue(reader, decoderContext, null));
        }
        reader.readEndArray();
        return list;
    }
}
