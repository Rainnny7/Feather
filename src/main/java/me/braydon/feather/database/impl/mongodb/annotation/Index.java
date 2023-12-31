/*
 * Copyright (c) 2023 Braydon (Rainnny). All rights reserved.
 *
 * For inquiries, please contact braydonrainnny@gmail.com
 */
package me.braydon.feather.database.impl.mongodb.annotation;

import me.braydon.feather.database.impl.mongodb.MongoDB;

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