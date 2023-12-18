/*
 * Copyright (c) 2023 Braydon (Rainnny). All rights reserved.
 *
 * For inquiries, please contact braydonrainnny@gmail.com
 */
package me.braydon.feather.database.impl.redis.annotation;

import java.lang.annotation.*;

/**
 * Entities tagged with this annotation will
 * expire after the amount of defined seconds.
 *
 * @author Braydon
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented @Inherited
public @interface TTL {
    /**
     * The time-to-live value.
     * <p>
     * If the value is zero or
     * below, then there will
     * be no expiration.
     * <p>
     * The key this TTL rule is assigned
     * to will expire in the defined amount
     * of seconds from when the key was updated.
     * </p>
     *
     * @return the value in seconds
     */
    long value() default -1L;
}