package me.braydon.feather.data;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import me.braydon.feather.FeatherSettings;
import me.braydon.feather.IDatabase;
import me.braydon.feather.annotation.Collection;
import me.braydon.feather.annotation.Field;
import me.braydon.feather.annotation.Id;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

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
     *
     * @see V for value type
     */
    private final Map<String, V> mappedData = Collections.synchronizedMap(new LinkedHashMap<>());
    
    public Document(@NonNull Object element, boolean rawObject) {
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
                Object value; // The value of the field
                
                if (fieldType == UUID.class) { // Convert UUIDs into strings
                    value = ((UUID) field.get(element)).toString();
                } else if (rawObject) { // Use the raw object from the field
                    value = field.get(element);
                } else { // Otherwise, turn the value into a string
                    if (fieldType == String.class) { // Already a string, cast it
                        value = field.get(element);
                    } else { // Convert the field into json using Gson
                        value = FeatherSettings.getGson().toJson(field.get(element));
                    }
                }
                mappedData.put(key, (V) value); // Store in our map
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
        assert idKey != null; // We need an id key
        this.idKey = idKey; // We have our id key
        
        V key = mappedData.get(idKey); // Get the id from the data map
        if (key == null) { // The element is missing an id field
            throw new IllegalArgumentException("No @Id annotated field found in " + clazz.getSimpleName());
        }
        this.key = key;
    }
}