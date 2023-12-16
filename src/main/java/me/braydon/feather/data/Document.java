/*
 * Copyright (c) 2023 Braydon (Rainnny). All rights reserved.
 *
 * For inquiries, please contact braydonrainnny@gmail.com
 */
package me.braydon.feather.data;

import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.ToString;
import me.braydon.feather.FeatherSettings;
import me.braydon.feather.annotation.Field;
import me.braydon.feather.annotation.Id;
import me.braydon.feather.annotation.RawData;
import me.braydon.feather.annotation.Serializable;
import me.braydon.feather.common.FieldUtils;
import me.braydon.feather.common.Tuple;
import me.braydon.feather.database.IDatabase;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A document is a key-value pair that is stored within
 * a collection. This document is based on the Bson
 * {@link org.bson.Document} in MongoDB, however this
 * document is universal between all {@link IDatabase}'s.
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
    
    @SneakyThrows
    public Document(@NonNull Object element) {
        Class<?> clazz = element.getClass(); // Get the element class
        String idKey = null; // The key for the id field
        java.lang.reflect.Field rawDataField = null;
        for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
            // Raw data field, save it for later
            if (field.isAnnotationPresent(RawData.class)) {
                rawDataField = field;
                continue;
            }
            // Field is missing the @Field annotation, skip it
            if (!field.isAnnotationPresent(Field.class)) {
                continue;
            }
            field.setAccessible(true); // Make our field accessible
            String key = FieldUtils.extractKey(field); // The key of the database field
            
            // The field is annotated with @Id, save it for later
            if (field.isAnnotationPresent(Id.class)) {
                idKey = key;
            }
            Class<?> fieldType = field.getType(); // The type of the field
            Object value = field.get(element); // The value of the field
            
            if (field.isAnnotationPresent(Serializable.class)) { // Serialize the field if @Serializable is present
                value = FeatherSettings.getGson().toJson(field.get(element));
            } else if (fieldType == UUID.class) { // Convert UUIDs into strings
                value = value.toString();
            }
            
            mappedData.put(key, new Tuple<>(field, (V) value)); // Store in our map
        }
        assert idKey != null; // We need an id key
        if (rawDataField != null) { // We have a raw data field, set it
            rawDataField.setAccessible(true); // Make our field accessible
            rawDataField.set(element, toMappedData()); // Set the raw data field to our mapped document
        }
        this.idKey = idKey; // Set our id key
        
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