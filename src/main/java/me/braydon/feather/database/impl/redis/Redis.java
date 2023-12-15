package me.braydon.feather.database.impl.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import lombok.NonNull;
import me.braydon.feather.database.IDatabase;

/**
 * The {@link IDatabase} implementation for Redis.
 *
 * @author Braydon
 * @see StatefulRedisConnection for the bootstrap class
 * @see RedisURI for the credentials class
 * @see RedisRepository for the repository class
 * @see <a href="https://redis.io">Redis Official Site</a>
 */
public class Redis implements IDatabase<StatefulRedisConnection<String, String>, RedisURI, RedisRepository<?, ?>> {
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
     * @see StatefulRedisConnection for bootstrap class
     */
    @Override
    public StatefulRedisConnection<String, String> getBootstrap() {
        return connection;
    }
    
    /**
     * Create a new repository
     * using this database.
     *
     * @return the repository instance
     * @see RedisRepository for repository
     */
    @Override @NonNull
    public <ID, E> RedisRepository<ID, E> newRepository() {
        return new RedisRepository<>(this);
    }
    
//    @Override
//    public void write(@NonNull Object element) {
//        if (!element.getClass().isAnnotationPresent(Collection.class)) { // Missing annotation
//            throw new IllegalStateException("Element is missing @Collection annotation");
//        }
//        Document<String> document = new Document<>(element); // Construct the document from the element
//        sync().hmset(String.valueOf(document.getKey()), document.toMappedData()); // Set the map in the database
//    }
    
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