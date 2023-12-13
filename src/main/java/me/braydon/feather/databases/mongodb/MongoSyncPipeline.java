package me.braydon.feather.databases.mongodb;

import com.mongodb.BasicDBObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * The pipeline for handling synchronous {@link MongoDB} operations.
 *
 * @author Braydon
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public final class MongoSyncPipeline {
    /**
     * The database to handle operations for.
     *
     * @see MongoDB for database
     */
    @NonNull private final MongoDB database;
    
    /**
     * Get the latency to the database.
     *
     * @return the latency
     */
    public long getPing() {
        if (!database.isConnected()) { // Not connected
            return -1L;
        }
        // Return ping
        long before = System.currentTimeMillis();
        database.getDatabase().runCommand(new BasicDBObject("ping", "1"));
        return System.currentTimeMillis() - before;
    }
}