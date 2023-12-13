package me.braydon.feather.databases.redis;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * The pipeline for handling {@link Redis} operations.
 *
 * @author Braydon
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public final class RedisPipeline {
    /**
     * The database to handle operations for.
     *
     * @see Redis for database
     */
    @NonNull private final Redis database;
}