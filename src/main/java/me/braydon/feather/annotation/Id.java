package me.braydon.feather.annotation;

import java.lang.annotation.*;

/**
 * {@link Field}'s tagged with this annotation will be
 * treated as the primary identifying key for documents
 * within a {@link Collection}.
 *
 * @author Braydon
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented @Inherited
public @interface Id {
    /**
     * The key of this field.
     *
     * @return the key
     */
    String key() default "_id";
}