package me.braydon.feather.database;

import lombok.NonNull;
import me.braydon.feather.repository.Repository;

import java.io.Closeable;

/**
 * Represents a database.
 *
 * @author Braydon
 * @param <B> the bootstrap class of this database
 * @param <C> the type of credentials this database uses
 * @param <R> the type of repository for this database
 */
public interface IDatabase<B, C, R extends Repository<?, ?, ?>> extends Closeable {
    /**
     * Get the name of this database.
     *
     * @return the database name
     */
    @NonNull String getName();
    
    /**
     * Initialize a connection to this database.
     *
     * @param credentials the optional credentials to use
     */
    void connect(C credentials);
    
    /**
     * Check if this database is connected.
     *
     * @return the database connection state
     */
    boolean isConnected();
    
    /**
     * Get the latency to this database.
     *
     * @return the latency, -1 if not connected
     */
    long getLatency();
    
    /**
     * Get the bootstrap class
     * instance for this database.
     *
     * @return the bootstrap class instance, null if none
     * @see B for bootstrap class
     */
    B getBootstrap();
    
    /**
     * Create a new repository
     * using this database.
     *
     * @param <ID> the id type
     * @param <E> the entity type
     * @return the repository instance
     * @see R for repository
     */
    @NonNull <ID, E> R newRepository();
}