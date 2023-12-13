package me.braydon.feather.databases.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import lombok.NonNull;
import me.braydon.feather.IDatabase;
import me.braydon.feather.annotation.Collection;
import me.braydon.feather.annotation.Field;
import me.braydon.feather.data.Document;

/**
 * The {@link IDatabase} implementation for MongoDB.
 *
 * @author Braydon
 * @see MongoClient for the bootstrap class
 * @see ConnectionString for the credentials class
 * @see MongoSyncPipeline for the sync pipeline class
 * @see MongoAsyncPipeline for the async pipeline class
 * @see <a href="https://www.mongodb.com">MongoDB Official Site</a>
 */
public class MongoDB implements IDatabase<MongoClient, ConnectionString, MongoSyncPipeline, MongoAsyncPipeline> {
    /**
     * The current {@link MongoClient} instance.
     */
    private MongoClient client;
    
    /**
     * The {@link MongoDatabase} instance.
     */
    @Getter private MongoDatabase database;
    
    /**
     * Get the name of this database.
     *
     * @return the database name
     */
    @Override @NonNull
    public String getName() {
        return "MongoDB";
    }
    
    /**
     * Initialize a connection to this database.
     *
     * @param credentials the optional credentials to use
     */
    @Override
    public void connect(ConnectionString credentials) {
        if (credentials == null) { // We need valid credentials
            throw new IllegalArgumentException("No credentials defined");
        }
        if (isConnected()) { // Already connected
            throw new IllegalStateException("Already connected");
        }
        String databaseName = credentials.getDatabase(); // Get the database name
        if (databaseName == null) {
            throw new IllegalArgumentException("A database name is required");
        }
        if (client != null) { // We have a client, close it first
            client.close();
        }
        client = MongoClients.create(credentials); // Create a new client
        database = client.getDatabase(databaseName); // Get the database
    }
    
    /**
     * Check if this database is connected.
     *
     * @return the database connection state
     */
    @Override
    public boolean isConnected() {
        return client != null;
    }
    
    /**
     * Get the bootstrap class
     * instance for this database.
     *
     * @return the bootstrap class instance, null if none
     * @see MongoClients for bootstrap class
     */
    @Override
    public MongoClient getBootstrap() {
        return client;
    }
    
    /**
     * Get the synchronized
     * pipeline for this database.
     *
     * @return the synchronized pipeline
     * @see MongoSyncPipeline for synchronized pipeline
     */
    @Override @NonNull
    public MongoSyncPipeline sync() {
        return new MongoSyncPipeline(this);
    }
    
    /**
     * Get the asynchronous
     * pipeline for this database.
     *
     * @return the asynchronous pipeline
     * @see MongoAsyncPipeline for asynchronous pipeline
     */
    @Override @NonNull
    public MongoAsyncPipeline async() {
        return new MongoAsyncPipeline(this);
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
        Class<?> clazz = element.getClass(); // Get the element class
        if (!clazz.isAnnotationPresent(Collection.class)) { // Missing annotation
            throw new IllegalStateException("Element is missing @Collection annotation");
        }
        Collection annotation = clazz.getAnnotation(Collection.class); // Get the @Collection annotation
        String collectionName = annotation.name(); // The name of the collection
        if (collectionName.isEmpty()) { // Missing collection name
            throw new IllegalStateException("Missing collection name in @Collection for " + clazz.getSimpleName());
        }
        MongoCollection<org.bson.Document> collection = database.getCollection(collectionName); // Get the collection
        Document<Object> document = new Document<>(element, true); // Construct the document from the element
        
        // Set the map in the database
        collection.updateOne(
            Filters.eq(document.getIdKey(), document.getKey()),
            new org.bson.Document("$set", new org.bson.Document(document.getMappedData())),
            new UpdateOptions().upsert(true)
        );
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
        client = null;
        database = null;
    }
}