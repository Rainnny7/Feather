package me.braydon.feather.database.impl.redis;

import lombok.NonNull;
import me.braydon.feather.repository.Repository;

import java.util.List;
import java.util.function.Predicate;

/**
 * The {@link Redis} {@link Repository} implementation.
 *
 * @author Braydon
 * @param <ID> the identifier for type for entities
 * @param <E> the entity type this repository stores
 */
public class RedisRepository<ID, E> extends Repository<Redis, ID, E> {
    public RedisRepository(@NonNull Redis database) {
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