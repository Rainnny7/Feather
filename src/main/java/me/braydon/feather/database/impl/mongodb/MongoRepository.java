/*
 * Copyright (c) 2023 Braydon (Rainnny). All rights reserved.
 *
 * For inquiries, please contact braydonrainnny@gmail.com
 */
package me.braydon.feather.database.impl.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import lombok.NonNull;
import lombok.SneakyThrows;
import me.braydon.feather.common.Tuple;
import me.braydon.feather.database.Repository;
import me.braydon.feather.database.impl.mongodb.annotation.CustomDocument;
import me.braydon.feather.database.impl.mongodb.annotation.Index;
import org.bson.Document;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * The {@link MongoDB} {@link Repository} implementation.
 *
 * @author Braydon
 * @param <ID> the identifier for type for entities
 * @param <E> the entity type this repository stores
 */
public class MongoRepository<ID, E> extends Repository<MongoDB, ID, E> {
    /**
     * The {@link MongoCollection} to use for this repository.
     */
    @NonNull private final MongoCollection<Document> collection;
    
    public MongoRepository(@NonNull MongoDB database, @NonNull Class<? extends E> entityClass, @NonNull String collectionName) {
        this(database, entityClass, database.getDatabase().getCollection(collectionName));
    }
    
    public MongoRepository(@NonNull MongoDB database, @NonNull Class<? extends E> entityClass, @NonNull MongoCollection<Document> collection) {
        super(database, entityClass);
        this.collection = collection;
    }
    
    /**
     * Get the entity with the given id.
     *
     * @param id the entity id
     * @return the entity with the id, null if none
     * @see ID for id
     * @see E for entity
     */
    @Override
    public E find(@NonNull ID id) {
        return find("_id", id);
    }
    
    /**
     * Get the entity with the given id.
     *
     * @param idKey the key of the id
     * @param id the entity id
     * @return the entity with the id, null if none
     * @see ID for id
     * @see E for entity
     */
    public E find(@NonNull String idKey, @NonNull ID id) {
        return newEntity(collection.find(new Document(idKey, id.toString())).first());
    }
    
    /**
     * Get all entities within this repository.
     *
     * @return the entities
     * @see E for entity
     */
    @Override
    public List<E> findAll() {
        List<E> entities = new ArrayList<>(); // The entities to return
        try (MongoCursor<Document> cursor = collection.find().cursor()) {
            while (cursor.hasNext()) { // Add the entity to the list
                entities.add(newEntity(cursor.next()));
            }
        }
        return Collections.unmodifiableList(entities);
    }
    
    /**
     * Save the given entities.
     *
     * @param entities the entities to save
     * @see E for entity
     */
    @Override @SneakyThrows
    public void saveAll(@NonNull E... entities) {
        List<UpdateOneModel<Document>> updateModels = new ArrayList<>(); // The update models to bulk write
        
        for (E entity : entities) {
            me.braydon.feather.data.Document document = new me.braydon.feather.data.Document(entity); // Create a document from the entity
            Document bsonDocument; // The Bson document to save
            
            Method customDocumentMethod = Arrays.stream(entity.getClass().getDeclaredMethods())
                                              .filter(method -> method.isAnnotationPresent(CustomDocument.class))
                                              .findFirst().orElse(null); // Get the @CustomDocument method
            
            // We have a custom document method
            if (customDocumentMethod != null && (customDocumentMethod.getReturnType() == Document.class)) {
                bsonDocument = (Document) customDocumentMethod.invoke(entity); // Get our custom document
            } else { // Otherwise, use our mapped data
                bsonDocument = new Document(document.toMappedData());
            }
            
            // Add our update model to the list
            updateModels.add(new UpdateOneModel<>(
                Filters.eq(document.getIdKey(), document.getKey()),
                new Document("$set", bsonDocument),
                new UpdateOptions().upsert(true)
            ));
            
            // Create indexes for @Index fields specified in the entity
            for (Map.Entry<String, Tuple<Field, Object>> mappedEntry : document.getMappedData().entrySet()) {
                java.lang.reflect.Field field = mappedEntry.getValue().getLeft();
                if (field.isAnnotationPresent(Index.class)) {
                    collection.createIndex(Indexes.text(mappedEntry.getKey()));
                }
            }
        }
        
        // We have update models to execute, bulk write them
        if (!updateModels.isEmpty()) {
            collection.bulkWrite(updateModels);
        }
    }
    
    /**
     * Get the amount of stored entities.
     *
     * @return the amount of stored entities
     * @see E for entity
     */
    @Override
    public long count() {
        return collection.countDocuments();
    }
    
    /**
     * Drop the entity with the given id.
     *
     * @param id the entity id to drop
     * @see ID for id
     * @see E for entity
     */
    @Override
    public void dropById(@NonNull ID id) {
        dropById("_id", id);
    }
    
    /**
     * Drop the entity with the given id.
     *
     * @param idKey the key of the id
     * @param id the entity id to drop
     * @see ID for id
     * @see E for entity
     */
    public void dropById(@NonNull String idKey, @NonNull ID id) {
        collection.deleteOne(new Document(idKey, id.toString())); // Delete the entity
    }
    
    /**
     * Drop the given entity.
     *
     * @param entity the entity to drop
     * @see E for entity
     */
    @Override
    public void drop(@NonNull E entity) {
        me.braydon.feather.data.Document document = new me.braydon.feather.data.Document(entity); // Create a document from the entity
        collection.deleteOne(new Document(document.getIdKey(), document.getKey())); // Delete the entity
    }
}