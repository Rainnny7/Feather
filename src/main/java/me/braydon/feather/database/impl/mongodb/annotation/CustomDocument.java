/*
 * Copyright (c) 2023 Braydon (Rainnny). All rights reserved.
 *
 * For inquiries, please contact braydonrainnny@gmail.com
 */
package me.braydon.feather.database.impl.mongodb.annotation;

import java.lang.annotation.*;

/**
 * Methods tagged with this annotation will be invoked
 * when a document is being saved to the database, this
 * allows is to save a custom document, rather than
 * parsing the entity and creating one that way.
 *
 * @author Braydon
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented @Inherited
public @interface CustomDocument { }