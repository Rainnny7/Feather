package me.braydon.feather.annotation;

import java.lang.annotation.*;

/**
 * @author Braydon
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented @Inherited
public @interface Serializable { }