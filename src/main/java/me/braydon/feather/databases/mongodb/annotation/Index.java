package me.braydon.feather.databases.mongodb.annotation;

import me.braydon.feather.databases.mongodb.MongoDB;

import java.lang.annotation.*;

/**
 * Fields flagged with this annotation will be
 * treated as an indexed field within {@link MongoDB}.
 *
 * @author Braydon
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented @Inherited
public @interface Index { }