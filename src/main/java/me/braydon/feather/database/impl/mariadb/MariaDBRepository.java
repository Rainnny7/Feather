/*
 * Copyright (c) 2023 Braydon (Rainnny). All rights reserved.
 *
 * For inquiries, please contact braydonrainnny@gmail.com
 */
package me.braydon.feather.database.impl.mariadb;

import lombok.NonNull;
import me.braydon.feather.database.Repository;

import java.util.List;

/**
 * The {@link MariaDB} {@link Repository} implementation.
 *
 * @author Braydon
 * @param <ID> the identifier for type for entities
 * @param <E> the entity type this repository stores
 */
public class MariaDBRepository<ID, E> extends Repository<MariaDB, ID, E> {
    public MariaDBRepository(@NonNull MariaDB database, @NonNull Class<? extends E> entityClass) {
        super(database, entityClass);
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
        throw new UnsupportedOperationException();
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
     * Drop the entity with the given id.
     *
     * @param id the entity id to drop
     * @see ID for id
     * @see E for entity
     */
    @Override
    public void dropById(@NonNull ID id) {
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