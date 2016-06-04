package eu.dozd.mongo;

import eu.dozd.mongo.annotation.Entity;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Class holding information about mapped classes and their fields.
 */
class EntityInfo {
    protected final Map<String, PropertyDescriptor> fields = new HashMap<>();
    protected final PropertyDescriptor[] descriptors;

    EntityInfo(Class<?> clazz) {
        try {
            descriptors = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        setDescriptors(Arrays.asList(descriptors));
    }

    Set<String> getFields() {
        return Collections.unmodifiableSet(fields.keySet());
    }

    String getIdField() {
        return null;
    }

    boolean isMappedReference(String field) {
        if (!fields.containsKey(field)) {
            return false;
        }

        PropertyDescriptor pd = getField(field);
        return pd.getPropertyType().isAnnotationPresent(Entity.class);
    }


    Class<?> getFieldType(String field) {
        return getField(field).getPropertyType();
    }

    void setValue(Object o, String field, Object v) {
        Method writeMethod = getField(field).getWriteMethod();
        if (writeMethod == null) {
            throw new MongoMapperException("Setter for property [" + field + "] in class [" + o.getClass().getCanonicalName() + "] not found.");
        }

        try {
            writeMethod.invoke(o, v);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new MongoMapperException("Cannot set value for property [" + field + "] in class [" + o.getClass().getCanonicalName() + "].", e);
        }
    }

    Object getValue(Object o, String field) {
        // Read method cannot be null because of check in constructor.
        Method readMethod = getField(field).getReadMethod();

        try {
            return readMethod.invoke(o);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new MongoMapperException("Cannot get value from property [" + field + "] in class [" + o.getClass().getCanonicalName() + "].", e);
        }
    }

    void setId(Object o, String id) {
    }

    String getId(Object o) {
        return null;
    }

    private PropertyDescriptor getField(String field) {
        PropertyDescriptor descriptor = fields.get(field);

        if (descriptor == null) {
            throw new MongoMapperException("Cannot find definition for property [" + field + "]. Are you missing getter?");
        }

        return descriptor;
    }

    private void setDescriptors(List<PropertyDescriptor> descriptors) {
        for (PropertyDescriptor descriptor : descriptors) {
            if (!"class".equals(descriptor.getName())) {
                fields.put(descriptor.getDisplayName(), descriptor);
            }
        }
    }
}
