/*
 * Copyright (c) 2023 Braydon (Rainnny). All rights reserved.
 *
 * For inquiries, please contact braydonrainnny@gmail.com
 */
package me.braydon.feather.annotation;

import com.google.gson.Gson;

import java.lang.annotation.*;

/**
 * {@link Field}'s tagged with this annotation
 * will have serialization handled by {@link Gson}.
 *
 * @author Braydon
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented @Inherited
public @interface Serializable { }