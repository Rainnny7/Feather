/*
 * Copyright (c) 2023 Braydon (Rainnny). All rights reserved.
 *
 * For inquiries, please contact braydonrainnny@gmail.com
 */
package me.braydon.feather.common;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import me.braydon.feather.annotation.Id;

import java.lang.reflect.Field;

/**
 * @author Braydon
 */
@UtilityClass
public final class FieldUtils {
    /**
     * Extract the key to use for the given field.
     *
     * @param field the field to get the key from
     * @return the key for the field
     */
    @NonNull
    public static String extractKey(@NonNull Field field) {
        boolean idField = field.isAnnotationPresent(Id.class); // Is this field annotated with @Id?
        me.braydon.feather.annotation.Field annotation = field.getAnnotation(me.braydon.feather.annotation.Field.class); // Get the @Field annotation
        String key = idField ? field.getAnnotation(Id.class).key() : annotation.key(); // The key of the database field
        if (key.isEmpty()) { // No field in the annotation, use the field name
            key = field.getName();
        }
        return key;
    }
}