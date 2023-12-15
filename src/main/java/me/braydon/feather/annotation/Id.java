/*
 * Copyright (c) 2023 Braydon (Rainnny). All rights reserved.
 *
 * For inquiries, please contact braydonrainnny@gmail.com
 */
package me.braydon.feather.annotation;

import me.braydon.feather.data.Document;

import java.lang.annotation.*;

/**
 * {@link Field}'s tagged with this annotation
 * will be treated as the primary identifying
 * key for {@link Document}'s within a collection.
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