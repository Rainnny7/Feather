package me.braydon.feather.repository;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import me.braydon.feather.IDatabase;

/**
 * A repository belonging to a {@link IDatabase}.
 *
 * @author Braydon
 * @param <D> the database
 */
@AllArgsConstructor @Getter(AccessLevel.PROTECTED)
public abstract class Repository<D extends IDatabase<?, ?, ?, ?>> {
    /**
     * The database this repository belongs to.
     *
     * @see D for database
     */
    @NonNull private final D database;
}