package me.braydon.feather;

import lombok.NonNull;

import java.io.Closeable;

/**
 * Represents a database.
 *
 * @author Braydon
 * @param <B> the bootstrap class of this database
 * @param <C> the type of credentials this database uses
 * @param <S> the type of sync pipeline for this database
 * @param <A> the type of async pipeline for this database
 */
public interface IDatabase<B, C, S, A> extends Closeable {
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
     * @see S for synchronized pipeline
     */
    @NonNull S sync();
    
    /**
     * Get the asynchronous
     * pipeline for this database.
     *
     * @return the asynchronous pipeline
     * @see A for asynchronous pipeline
     */
    @NonNull A async();
}