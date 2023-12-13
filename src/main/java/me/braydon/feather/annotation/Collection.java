package me.braydon.feather.annotation;

import java.lang.annotation.*;

/**
 * Classes tagged with this annotation
 * will be treated as a collection.
 *
 * @author Braydon
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented @Inherited
public @interface Collection {
    /**
     * The name of this collection.
     *
     * @return the name
     */
    String name() default "";
}