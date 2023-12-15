package me.braydon.feather.repository;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import me.braydon.feather.database.IDatabase;

import java.util.List;
import java.util.function.Predicate;

/**
 * A repository belonging to a {@link IDatabase}.
 *
 * @author Braydon
 * @param <D> the database this repository uses
 * @param <ID> the identifier for type for entities
 * @param <E> the entity type this repository stores
 */
@AllArgsConstructor @Getter(AccessLevel.PROTECTED)
public abstract class Repository<D extends IDatabase<?, ?, ?>, ID, E> {
    /**
     * The database this repository belongs to.
     *
     * @see D for database
     */
    @NonNull private final D database;
    
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
     * Find the entity matching the given predicate.
     *
     * @param predicate the predicate to test
     * @return the found entity
     * @see E for entity
     * @see Predicate for predicate
     */
    public abstract E findOne(@NonNull Predicate<E> predicate);
    
    /**
     * Find all entities matching the given predicate.
     *
     * @param predicate the predicate to test
     * @return the found entities
     * @see E for entity
     * @see Predicate for predicate
     */
    public abstract List<E> findAll(@NonNull Predicate<E> predicate);
    
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
}