package eu.dozd.mongo;

import eu.dozd.mongo.annotation.Entity;
import eu.dozd.mongo.annotation.Id;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Class holding information about mapped classes and their fields.
 */
class EntityInfo {
    private final String idColumn;
    private final Map<String, PropertyDescriptor> fields = new HashMap<>();

    public EntityInfo(Class<?> clazz) {
        PropertyDescriptor[] descriptors;
        try {
            descriptors = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }

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

        this.idColumn = idColumn;
        setDescriptors(Arrays.asList(descriptors));
    }

    public Set<String> getFields() {
        return Collections.unmodifiableSet(fields.keySet());
    }

    public String getIdColumn() {
        return idColumn;
    }

    public boolean isMappedReference(String field) {
        if (! fields.containsKey(field)) {
            return false;
        }

        PropertyDescriptor pd = getField(field);
        return pd.getPropertyType().isAnnotationPresent(Entity.class);
    }


    public Class<?> getFieldType(String field) {
        return getField(field).getPropertyType();
    }

    public void setValue(Object o, String field, Object v) {
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

    public Object getValue(Object o, String field) {
        // Read method cannot be null because of check in constructor.
        Method readMethod = getField(field).getReadMethod();

        try {
            return readMethod.invoke(o);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new MongoMapperException("Cannot get value from property [" + field + "] in class [" + o.getClass().getCanonicalName() + "].", e);
        }
    }

    public void setId(Object o, String id) {
        setValue(o, idColumn, id);
    }

    public String getId(Object o) {
        return (String) getValue(o, idColumn);
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
