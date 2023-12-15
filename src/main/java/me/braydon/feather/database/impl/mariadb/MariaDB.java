/*
 * Copyright (c) 2023 Braydon (Rainnny). All rights reserved.
 *
 * For inquiries, please contact braydonrainnny@gmail.com
 */
package me.braydon.feather.database.impl.mariadb;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.NonNull;
import me.braydon.feather.database.IDatabase;

/**
 * The {@link IDatabase} implementation for MariaDB (& other MySQL servers).
 *
 * @author Braydon
 * @see HikariDataSource for the bootstrap class
 * @see MariaDBAuthorization for the credentials class
 * @see <a href="https://github.com/brettwooldridge/HikariCP">HikariCP Official GitHub</a>
 */
public class MariaDB implements IDatabase<HikariDataSource, MariaDBAuthorization> {
    /**
     * The current {@link HikariDataSource} instance.
     */
    private HikariDataSource dataSource;
    
    /**
     * Get the name of this database.
     *
     * @return the database name
     */
    @Override @NonNull
    public String getName() {
        return "MariaDB";
    }
    
    /**
     * Initialize a connection to this database.
     *
     * @param credentials the optional credentials to use
     * @throws IllegalArgumentException if no credentials are provided
     * @throws IllegalStateException if already connected
     * @see MariaDBAuthorization for credentials
     */
    @Override
    public void connect(MariaDBAuthorization credentials) throws IllegalArgumentException, IllegalStateException {
        if (credentials == null) { // We need valid credentials
            throw new IllegalArgumentException("No credentials defined");
        }
        if (isConnected()) { // Already connected
            throw new IllegalStateException("Already connected");
        }
        if (dataSource != null) { // We have a data source, close it first
            dataSource.close();
        }
        HikariConfig config = credentials.getHikariConfig(); // Get the custom config
        if (config == null) { // No custom config, make a new one
            config = new HikariConfig();
        }
        config.setJdbcUrl(credentials.getJdbcUrl()); // Set the JDBC connection URL
        config.setUsername(credentials.getUsername()); // Set the username
        config.setPassword(credentials.getPassword()); // Set the password
        dataSource = new HikariDataSource(config); // Create a new data source
    }
    
    /**
     * Check if this database is connected.
     *
     * @return the database connection state
     */
    @Override
    public boolean isConnected() {
        return dataSource != null && (dataSource.isRunning() && !dataSource.isClosed());
    }
    
    /**
     * Get the latency to this database.
     *
     * @return the latency, -1 if not connected
     */
    @Override
    public long getLatency() {
        return 0;
    }
    
    /**
     * Get the bootstrap class
     * instance for this database.
     *
     * @return the bootstrap class instance, null if none
     * @see HikariDataSource for bootstrap class
     */
    @Override
    public HikariDataSource getBootstrap() {
        return dataSource;
    }
    
    /**
     * Closes this stream and releases any system resources associated
     * with it. If the stream is already closed then invoking this
     * method has no effect.
     */
    @Override
    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
        dataSource = null;
    }
}