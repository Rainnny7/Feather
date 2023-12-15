/*
 * Copyright (c) 2023 Braydon (Rainnny). All rights reserved.
 *
 * For inquiries, please contact braydonrainnny@gmail.com
 */
package me.braydon.feather.database;

import lombok.NonNull;

import java.io.Closeable;

/**
 * Represents a database.
 *
 * @author Braydon
 * @param <B> the bootstrap class of this database
 * @param <C> the type of credentials this database uses
 */
public interface IDatabase<B, C> extends Closeable {
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
     * @throws IllegalStateException if already connected
     */
    void connect(C credentials) throws IllegalStateException;
    
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
}