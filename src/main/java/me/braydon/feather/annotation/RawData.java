/*
 * Copyright (c) 2023 Braydon (Rainnny). All rights reserved.
 *
 * For inquiries, please contact braydonrainnny@gmail.com
 */
package me.braydon.feather.annotation;

import java.lang.annotation.*;

/**
 * {@link Field}'s tagged with this annotation
 * will be set to the raw data from the document
 * it is in.
 *
 * @author Braydon
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented @Inherited
public @interface RawData { }