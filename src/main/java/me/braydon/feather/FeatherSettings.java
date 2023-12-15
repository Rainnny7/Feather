package me.braydon.feather;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;

/**
 * Settings for feather, modify these as you wish!
 *
 * @author Braydon
 */
public final class FeatherSettings {
    /**
     * The {@link Gson} instance to use for serialization.
     */
    @Setter @Getter private static Gson gson = new GsonBuilder()
                                                   .serializeNulls()
                                                   .create();
}