package eu.dozd.mongo;

import eu.dozd.mongo.annotation.Id;
import org.bson.types.ObjectId;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class EntityInfoWithId extends EntityInfo {

    private final String idField;

    EntityInfoWithId(Class<?> clazz) {
        super(clazz);

        // Find ID property.
        String idColumn = null;
        for (PropertyDescriptor pd : descriptors) {

            if (pd.getReadMethod() != null && !"class".equals(pd.getName())) {
                Annotation[] declaredAnnotations = pd.getReadMethod().getDeclaredAnnotations();
                for (Annotation annotation : declaredAnnotations) {
                    if (annotation.annotationType().equals(Id.class)) {
                        idColumn = pd.getDisplayName();
                        break;
                    }
                }
            }
        }

        if (idColumn == null) {
            idColumn = findIdAnnotation(clazz);
            if (idColumn == null) {
                throw new MongoMapperException("No ID field defined on class " + clazz.getCanonicalName());
            }
        }

        idField = idColumn;
    }

    private String findIdAnnotation(Class<?> klass) {
        for (Field field : klass.getDeclaredFields()) {
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                if (annotation.annotationType().equals(Id.class)) {
                    return field.getName();
                }
            }
        }
        return null;
    }

    void setId(Object o, Object id) {
        if (getFieldType(idField).equals(String.class) && id instanceof ObjectId) {
            setValue(o, idField, id.toString());
        } else {
            setValue(o, idField, id);
        }
    }

    Object getId(Object o) {
        return getValue(o, idField);
    }

    String getIdField() {
        return idField;
    }
}
