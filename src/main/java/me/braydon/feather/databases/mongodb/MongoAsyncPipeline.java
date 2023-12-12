package me.braydon.feather.databases.mongodb;

import com.mongodb.BasicDBObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import me.braydon.feather.FeatherThreads;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * The pipeline for handling asynchronous {@link MongoDB} operations.
 *
 * @author Braydon
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public final class MongoAsyncPipeline {
    /**
     * The database to handle operations for.
     */
    @NonNull private final MongoDB database;
    
    /**
     * Get the latency to the database.
     *
     * @return the latency
     */
    public CompletableFuture<Long> getPing() {
        return getPing(FeatherThreads.THREAD_POOL);
    }
    
    /**
     * Get the latency to the database.
     *
     * @param executor the thread executor to use
     * @return the latency
     * @see Executor for executor
     */
    public CompletableFuture<Long> getPing(@NonNull Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            if (!database.isConnected()) { // Not connected
                return -1L;
            }
            // Return ping
            long before = System.currentTimeMillis();
            database.getDatabase().runCommand(new BasicDBObject("ping", "1"));
            return System.currentTimeMillis() - before;
        }, executor);
    }
}