package me.braydon.feather.annotation;

import java.lang.annotation.*;

/**
 * Fields tagged with this annotation will be
 * treated as a field within a {@link Collection}.
 *
 * @author Braydon
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE, ElementType.FIELD })
@Documented @Inherited
public @interface Field {
    /**
     * The key of this field.
     * <p>
     * If empty, the field name
     * will be used as the key.
     * </p>
     *
     * @return the key
     */
    String key() default "";
}