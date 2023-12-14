package me.braydon.feather.data;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import me.braydon.feather.FeatherSettings;
import me.braydon.feather.IDatabase;
import me.braydon.feather.annotation.Collection;
import me.braydon.feather.annotation.Field;
import me.braydon.feather.annotation.Id;
import me.braydon.feather.annotation.Serializable;
import me.braydon.feather.common.Tuple;

import java.util.*;

/**
 * A document is a key-value pair that is stored
 * within a {@link Collection}. This document is
 * based on the Bson {@link org.bson.Document}
 * in MongoDB, however this document is universal
 * between all {@link IDatabase}'s.
 *
 * @author Braydon
 * @param <V> the type of value this document holds
 */
@Getter @ToString
public class Document<V> {
    /**
     * The key to use for the id field.
     */
    @NonNull private final String idKey;
    
    /**
     * The key of this document.
     */
    @NonNull private final Object key;
    
    /**
     * The mapped data of this document.
     * <p>
     * The key of this key-value pair is the identifier
     * for the field within this document. The value is
     * a tuple that contains the Java field, as well as
     * the field value.
     * </p>
     *
     * @see V for value type
     */
    private final Map<String, Tuple<java.lang.reflect.Field, V>> mappedData = Collections.synchronizedMap(new LinkedHashMap<>());
    
    public Document(@NonNull Object element) {
        Class<?> clazz = element.getClass(); // Get the element class
        String idKey = null; // The key for the id field
        for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
            // Field is missing the @Field annotation, skip it
            if (!field.isAnnotationPresent(Field.class)) {
                continue;
            }
            field.setAccessible(true); // Make our field accessible
            boolean idField = field.isAnnotationPresent(Id.class); // Is this field annotated with @Id?
            Field annotation = field.getAnnotation(Field.class); // Get the @Field annotation
            String key = idField ? field.getAnnotation(Id.class).key() : annotation.key(); // The key of the database field
            if (key.isEmpty()) { // No field in the annotation, use the field name
                key = field.getName();
            }
            // The field is annotated with @Id, save it for later
            if (idField) {
                idKey = key;
            }
            Class<?> fieldType = field.getType(); // The type of the field
            try {
                Object value = field.get(element); // The value of the field
                
                if (field.isAnnotationPresent(Serializable.class)) { // Serialize the field if @Serializable is present
                    value = FeatherSettings.getGson().toJson(field.get(element));
                } else if (fieldType == UUID.class) { // Convert UUIDs into strings
                    value = ((UUID) value).toString();
                }
                
                mappedData.put(key, new Tuple<>(field, (V) value)); // Store in our map
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
        assert idKey != null; // We need an id key
        this.idKey = idKey; // We have our id key
        
        Tuple<java.lang.reflect.Field, V> key = mappedData.get(idKey); // Get the id from the data map
        if (key == null) { // The element is missing an id field
            throw new IllegalArgumentException("No @Id annotated field found in " + clazz.getSimpleName());
        }
        this.key = key.getRight();
    }
    
    /**
     * Turn this document into a map.
     *
     * @return the mapped data
     * @see #mappedData for stored data
     */
    @NonNull
    public Map<String, V> toMappedData() {
        Map<String, V> mappedData = new LinkedHashMap<>(); // The mapped data
        for (Map.Entry<String, Tuple<java.lang.reflect.Field, V>> entry : this.mappedData.entrySet()) {
            mappedData.put(entry.getKey(), entry.getValue().getRight());
        }
        return mappedData;
    }
}