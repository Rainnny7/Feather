/*
 * Copyright (c) 2023 Braydon (Rainnny). All rights reserved.
 *
 * For inquiries, please contact braydonrainnny@gmail.com
 */
package me.braydon.feather.repository;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import me.braydon.feather.FeatherSettings;
import me.braydon.feather.annotation.Serializable;
import me.braydon.feather.common.FieldUtils;
import me.braydon.feather.database.IDatabase;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * A repository belonging to a {@link IDatabase}.
 *
 * @author Braydon
 * @param <D> the database this repository uses
 * @param <ID> the identifier for type for entities
 * @param <E> the entity type this repository stores
 */
@AllArgsConstructor @Getter(AccessLevel.PROTECTED)
public abstract class Repository<D extends IDatabase<?, ?>, ID, E> {
    /**
     * The database this repository belongs to.
     *
     * @see D for database
     */
    @NonNull private final D database;
    
    /**
     * The class for the entity this repository uses.
     *
     * @see E for entity
     */
    @NonNull private final Class<? extends E> entityClass;
    
    /**
     * Get the entity with the given id.
     *
     * @param id the entity id
     * @return the entity with the id, null if none
     * @see ID for id
     * @see E for entity
     */
    public abstract E find(@NonNull ID id);
    
    /**
     * Get all entities within this repository.
     *
     * @return the entities
     * @see E for entity
     */
    public abstract List<E> findAll();
    
    /**
     * Save the given entity to the database.
     *
     * @param entity the entity to save
     * @see E for entity
     */
    public void save(@NonNull E entity) {
        saveAll(entity);
    }
    
    /**
     * Save the given entities.
     *
     * @param entities the entities to save
     * @see E for entity
     */
    public abstract void saveAll(@NonNull E... entities);
    
    /**
     * Get the amount of stored entities.
     *
     * @return the amount of stored entities
     * @see E for entity
     */
    public abstract long count();
    
    /**
     * Drop the given entity.
     *
     * @param entity the entity to drop
     * @see E for entity
     */
    public abstract void drop(@NonNull E entity);
    
    /**
     * Construct a new entity from the given mapped data.
     *
     * @param mappedData the mapped data to parse
     * @return the created entity, null if none
     * @see E for entity
     */
    protected final E newEntity(Map<String, Object> mappedData) {
        if (mappedData == null) { // No mapped data given
            return null;
        }
        try {
            Constructor<? extends E> constructor = entityClass.getConstructor(); // Get the no args constructor
            E entity = constructor.newInstance(); // Create the entity
            
            // Get the field tagged with @Id
            for (Field field : entityClass.getDeclaredFields()) {
                String key = FieldUtils.extractKey(field); // The key of the database field
                Class<?> type = field.getType(); // The type of the field
                Object value = mappedData.get(key); // The value of the field
                
                // Field is serializable and is a string, deserialize it using Gson
                if (field.isAnnotationPresent(Serializable.class) && value.getClass() == String.class) {
                    value = FeatherSettings.getGson().fromJson((String) value, type);
                } else if (type == UUID.class) { // Type is a UUID, convert it
                    value = UUID.fromString((String) value);
                }
                
                // Set the value of the field
                field.setAccessible(true);
                field.set(entity, value);
            }
            return entity;
        } catch (NoSuchMethodException ex) { // We need our no args constructor
            throw new IllegalStateException("Entity " + entityClass.getName() + " is missing no args constructor");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}