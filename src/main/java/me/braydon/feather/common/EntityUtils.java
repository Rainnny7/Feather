package me.braydon.feather.common;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import me.braydon.feather.annotation.Collection;

/**
 * @author Braydon
 */
@UtilityClass
public final class EntityUtils {
    /**
     * Ensure that the given entity is valid.
     *
     * @param entity the entity to validate
     * @param allowEmptyCollections should empty collections be allowed?
     */
    public static void ensureValid(@NonNull Object entity, boolean allowEmptyCollections) {
        Class<?> clazz = entity.getClass(); // Get the element class
        if (!clazz.isAnnotationPresent(Collection.class)) { // Missing annotation
            throw new IllegalStateException("Element is missing @Collection annotation");
        }
        Collection annotation = clazz.getAnnotation(Collection.class); // Get the @Collection annotation
        String collectionName = annotation.name(); // The name of the collection
        if (collectionName.isEmpty() && !allowEmptyCollections) { // Missing collection name
            throw new IllegalStateException("Missing collection name in @Collection for " + clazz.getSimpleName());
        }
    }
}