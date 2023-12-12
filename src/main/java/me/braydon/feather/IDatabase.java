package me.braydon.feather;

import lombok.NonNull;

import java.io.Closeable;

/**
 * Represents a database.
 *
 * @author Braydon
 * @param <B> the bootstrap class of this database
 * @param <A> the type of credentials this database uses
 * @param <P> the type of pipeline for this database
 * @param <AP> the type of async pipeline for this database
 */
public interface IDatabase<B, A, P, AP> extends Closeable {
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
    void connect(A credentials);
    
    /**
     * Check if this database is connected.
     *
     * @return the database connection state
     */
    boolean isConnected();
    
    /**
     * Get the bootstrap class
     * instance for this database.
     *
     * @return the bootstrap class instance, null if none
     * @see B for bootstrap class
     */
    B getBootstrap();
    
    /**
     * Get the synchronized
     * pipeline for this database.
     *
     * @return the synchronized pipeline
     * @see P for synchronized pipeline
     */
    @NonNull P sync();
    
    /**
     * Get the asynchronous
     * pipeline for this database.
     *
     * @return the asynchronous pipeline
     * @see AP for asynchronous pipeline
     */
    @NonNull AP async();
}