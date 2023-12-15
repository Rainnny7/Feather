package me.braydon.feather.database.impl.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import lombok.NonNull;
import me.braydon.feather.annotation.Collection;
import me.braydon.feather.common.EntityUtils;
import me.braydon.feather.common.Tuple;
import me.braydon.feather.database.impl.mongodb.annotation.Index;
import me.braydon.feather.repository.Repository;
import org.bson.Document;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * The {@link MongoDB} {@link Repository} implementation.
 *
 * @author Braydon
 * @param <ID> the identifier for type for entities
 * @param <E> the entity type this repository stores
 */
public class MongoRepository<ID, E> extends Repository<MongoDB, ID, E> {
    public MongoRepository(@NonNull MongoDB database) {
        super(database);
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
        String idString = id.toString(); // Stringify the ID
        throw new UnsupportedOperationException();
    }
    
    /**
     * Find the entity matching the given predicate.
     *
     * @param predicate the predicate to test
     * @return the found entity
     * @see E for entity
     * @see Predicate for predicate
     */
    @Override
    public E findOne(@NonNull Predicate<E> predicate) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Find all entities matching the given predicate.
     *
     * @param predicate the predicate to test
     * @return the found entities
     * @see E for entity
     * @see Predicate for predicate
     */
    @Override
    public List<E> findAll(@NonNull Predicate<E> predicate) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Get all entities within this repository.
     *
     * @return the entities
     * @see E for entity
     */
    @Override
    public List<E> findAll() {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Save the given entities.
     *
     * @param entities the entities to save
     * @see E for entity
     */
    @Override
    public void saveAll(@NonNull E... entities) {
        Map<String, List<me.braydon.feather.data.Document<Object>>> toSave = new HashMap<>(); // The documents to save
        
        // Iterate over the given entities and ensure they
        // are all valid, and if they are, collect them so
        // we can bulk save them later
        for (E entity : entities) {
            EntityUtils.ensureValid(entity, false); // Ensure our entity is valid
            String collectionName = entity.getClass().getAnnotation(Collection.class).name(); // The name of the collection
            
            // Add the document to our list of documents to save
            List<me.braydon.feather.data.Document<Object>> documents = toSave.getOrDefault(collectionName, new ArrayList<>());
            documents.add(new me.braydon.feather.data.Document<>(entity));
            toSave.put(collectionName, documents);
        }
        
        // Iterate over the documents we want to save, and create
        // an update model for them, as well as update indexes.
        for (Map.Entry<String, List<me.braydon.feather.data.Document<Object>>> entry : toSave.entrySet()) {
            MongoCollection<Document> collection = getDatabase().getDatabase().getCollection(entry.getKey()); // The collection to save to
            
            List<UpdateOneModel<Document>> updateModels = new ArrayList<>();
            for (me.braydon.feather.data.Document<Object> document : entry.getValue()) {
                // Add or update model to the list
                updateModels.add(new UpdateOneModel<>(
                    Filters.eq(document.getIdKey(), document.getKey()),
                    new Document("$set", new Document(document.toMappedData())),
                    new UpdateOptions().upsert(true)
                ));
                
                // Create indexes for @Index fields
                for (Map.Entry<String, Tuple<Field, Object>> mappedEntry : document.getMappedData().entrySet()) {
                    java.lang.reflect.Field field = mappedEntry.getValue().getLeft();
                    if (field.isAnnotationPresent(Index.class)) {
                        collection.createIndex(Indexes.text(mappedEntry.getKey()));
                    }
                }
            }
            
            // We have updates models present, bulk write them to the database
            if (!updateModels.isEmpty()) {
                collection.bulkWrite(updateModels);
            }
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
        throw new UnsupportedOperationException();
    }
    
    /**
     * Drop the given entity.
     *
     * @param entity the entity to drop
     * @see E for entity
     */
    @Override
    public void drop(@NonNull E entity) {
        throw new UnsupportedOperationException();
    }
}