/*
 * Copyright (c) 2023 Braydon (Rainnny). All rights reserved.
 *
 * For inquiries, please contact braydonrainnny@gmail.com
 */
package me.braydon.feather.common;

import lombok.*;

/**
 * Represents an object that
 * holds a pair of two values.
 *
 * @author Braydon
 * @param <L> the left value
 * @param <R> the right value
 */
@NoArgsConstructor @AllArgsConstructor @Setter @Getter @ToString
public class Tuple<L, R> {
    /**
     * The left value of this tuple.
     */
    private L left;
    
    /**
     * The right value of this tuple.
     */
    private R right;
}