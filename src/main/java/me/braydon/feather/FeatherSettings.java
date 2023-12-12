package me.braydon.feather;

import lombok.Getter;
import lombok.Setter;

/**
 * Settings for feather, modify these as you wish!
 *
 * @author Braydon
 */
public final class FeatherSettings {
    /**
     * The amount of threads to use for {@link FeatherThreads}.
     */
    @Setter @Getter private static int threadCount = 4;
}