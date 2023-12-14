package me.braydon.feather.databases.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.NonNull;
import me.braydon.feather.IDatabase;
import me.braydon.feather.annotation.Collection;
import me.braydon.feather.annotation.Field;
import me.braydon.feather.data.Document;

/**
 * The {@link IDatabase} implementation for Redis.
 *
 * @author Braydon
 * @see StatefulRedisConnection for the bootstrap class
 * @see RedisURI for the credentials class
 * @see RedisCommands for the sync pipeline class
 * @see RedisAsyncCommands for the async pipeline class
 * @see <a href="https://redis.io">Redis Official Site</a>
 */
public class Redis implements IDatabase<StatefulRedisConnection<String, String>, RedisURI, RedisCommands<String, String>, RedisAsyncCommands<String, String>> {
    /**
     * The current {@link RedisClient} instance.
     */
    private RedisClient client;
    
    /**
     * The current established {@link StatefulRedisConnection}.
     */
    private StatefulRedisConnection<String, String> connection;
    
    /**
     * Get the name of this database.
     *
     * @return the database name
     */
    @Override @NonNull
    public String getName() {
        return "Redis";
    }
    
    /**
     * Initialize a connection to this database.
     *
     * @param credentials the optional credentials to use
     */
    @Override
    public void connect(RedisURI credentials) {
        if (credentials == null) { // We need valid credentials
            throw new IllegalArgumentException("No credentials defined");
        }
        if (isConnected()) { // Already connected
            throw new IllegalStateException("Already connected");
        }
        if (client != null) { // We have a client, close it first
            client.close();
        }
        if (connection != null) { // We have a connection, close it first
            connection.close();
        }
        client = RedisClient.create(credentials); // Create a new client
        connection = client.connect(); // Connect to the Redis server
    }
    
    /**
     * Check if this database is connected.
     *
     * @return the database connection state
     */
    @Override
    public boolean isConnected() {
        return client != null && (connection != null && connection.isOpen());
    }
    
    /**
     * Get the bootstrap class
     * instance for this database.
     *
     * @return the bootstrap class instance, null if none
     * @see StatefulRedisConnection for bootstrap class
     */
    @Override
    public StatefulRedisConnection<String, String> getBootstrap() {
        return connection;
    }
    
    /**
     * Get the synchronized
     * pipeline for this database.
     *
     * @return the synchronized pipeline
     * @see RedisPipeline for synchronized pipeline
     */
    @Override @NonNull
    public RedisCommands<String, String> sync() {
        return connection.sync();
    }
    
    /**
     * Get the asynchronous
     * pipeline for this database.
     *
     * @return the asynchronous pipeline
     * @see RedisPipeline for asynchronous pipeline
     */
    @Override @NonNull
    public RedisAsyncCommands<String, String> async() {
        return connection.async();
    }
    
    /**
     * Write the given object to the database.
     * <p>
     * This object is an instance of a class
     * annotated with {@link Collection}, and
     * contains fields annotated with {@link Field}.
     * </p>
     *
     * @param element the element to write
     */
    @Override
    public void write(@NonNull Object element) {
        if (!element.getClass().isAnnotationPresent(Collection.class)) { // Missing annotation
            throw new IllegalStateException("Element is missing @Collection annotation");
        }
        Document<String> document = new Document<>(element); // Construct the document from the element
        sync().hmset(String.valueOf(document.getKey()), document.toMappedData()); // Set the map in the database
    }
    
    /**
     * Closes this stream and releases any system resources associated
     * with it. If the stream is already closed then invoking this
     * method has no effect.
     */
    @Override
    public void close() {
        if (client != null) {
            client.close();
        }
        if (connection != null) {
            connection.close();
        }
        client = null;
        connection = null;
    }
}