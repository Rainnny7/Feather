package me.braydon.feather.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents an object that
 * holds a pair of two values.
 *
 * @author Braydon
 * @param <L> the left value
 * @param <R> the right value
 */
@NoArgsConstructor @AllArgsConstructor @Setter @Getter
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