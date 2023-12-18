/*
 * Copyright (c) 2023 Braydon (Rainnny). All rights reserved.
 *
 * For inquiries, please contact braydonrainnny@gmail.com
 */
package me.braydon.feather.database.impl.redis;

import io.lettuce.core.api.sync.RedisCommands;
import lombok.NonNull;
import me.braydon.feather.data.Document;
import me.braydon.feather.database.Repository;
import me.braydon.feather.database.impl.redis.annotation.TTL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The {@link Redis} {@link Repository} implementation.
 *
 * @author Braydon
 * @param <ID> the identifier for type for entities
 * @param <E> the entity type this repository stores
 */
public class RedisRepository<ID, E> extends Repository<Redis, ID, E> {
    /**
     * The prefix to use for keys in this repository.
     */
    @NonNull private final String keyPrefix;
    
    public RedisRepository(@NonNull Redis database, @NonNull Class<? extends E> entityClass, @NonNull String keyPrefix) {
        super(database, entityClass);
        this.keyPrefix = keyPrefix.trim();
        if (this.keyPrefix.isEmpty()) { // Missing a key prefix
            throw new IllegalArgumentException("Missing key prefix");
        }
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
        return newEntity(getDatabase().getBootstrap().sync().hgetall(keyPrefix + ":" + id));
    }
    
    /**
     * Get all entities within this repository.
     *
     * @return the entities
     * @see E for entity
     */
    @Override
    public List<E> findAll() {
        RedisCommands<String, String> commands = getDatabase().getBootstrap().sync(); // The sync command executor
        List<E> entities = new ArrayList<>(); // The entities to return
        for (String key : commands.keys(keyPrefix + ":*")) {
            entities.add(newEntity(commands.hgetall(key)));
        }
        return Collections.unmodifiableList(entities);
    }
    
    /**
     * Save the given entities.
     *
     * @param entities the entities to save
     * @see E for entity
     */
    @Override
    public void saveAll(@NonNull E... entities) {
        boolean multi = entities.length > 1; // Should we run multiple commands?
        RedisCommands<String, String> commands = getDatabase().getBootstrap().sync(); // The sync command executor
        if (multi) { // Prepare for setting the entities
            commands.multi();
        }
        for (E entity : entities) { // Set our entities
            Document<String> document = new Document<>(entity); // Create a document from the entity
            String key = keyPrefix + ":" + document.getKey(); // The key of this entity
            commands.hmset(key, document.toMappedData()); // Set the mapped document in the database
            
            // Handling @TTL annotations
            Class<?> clazz = entity.getClass(); // The entity class
            if (!clazz.isAnnotationPresent(TTL.class)) { // Missing @TTL
                continue;
            }
            long ttl = clazz.getAnnotation(TTL.class).value(); // Get the ttl value
            if (ttl > 0L) { // Value is above zero, set it
                commands.expire(key, ttl);
            }
        }
        if (multi) { // Execute the commands in bulk
            commands.exec();
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
        return findAll().size();
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
        getDatabase().getBootstrap().sync().del(keyPrefix + ":" + id);
    }
    
    /**
     * Drop the given entity.
     *
     * @param entity the entity to drop
     * @see E for entity
     */
    @Override
    public void drop(@NonNull E entity) {
        me.braydon.feather.data.Document<Object> document = new me.braydon.feather.data.Document<>(entity); // Create a document from the entity
        getDatabase().getBootstrap().sync().del(keyPrefix + ":" + document.getKey());
    }
}