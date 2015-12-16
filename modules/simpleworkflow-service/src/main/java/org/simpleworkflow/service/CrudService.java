package org.simpleworkflow.service;

import java.util.List;
import java.util.Set;

import org.hibernate.criterion.Example;
import org.simpleworkflow.domain.AbstractEntity;
import org.simpleworkflow.repository.support.ExampleCrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface CrudService<T extends AbstractEntity, R extends ExampleCrudRepository<T, Long>> {
	/**
	 * Set repository
	 *
	 * @param repository
	 *            Repositoty
	 */
	void setRepository(R repository);
	/**
	 * Create new resource.
	 *
	 * @param resource
	 *            Resource to create
	 * @return new resource
	 */
	T create(T resource);

	/**
	 * Update existing resource.
	 *
	 * @param resource
	 *            Resource to update
	 * @return resource updated
	 */
	T update(T resource);

	/**
	 * Save(upsert) resource.
	 *
	 * @param resource
	 *            Resource to update
	 * @return resource updated
	 */
	T save(T resource);

	/**
	 * Delete existing resource.
	 *
	 * @param resource
	 *            Resource to delete
	 */
	void delete(T resource);

	/**
	 * Delete existing resource.
	 *
	 * @param id
	 *            Resource id
	 */
	void delete(Long id);

	/**
	 * Find resource by id.
	 *
	 * @param id
	 *            Resource id
	 * @return resource
	 */
	T findOne(Long id);

	/**
	 * Find resources by their ids.
	 *
	 * @param ids
	 *            Resource ids
	 * @return a list of retrieved resources, empty if no resource found
	 */
	List<T> findByIds(Set<Long> ids);

	/**
	 * Find all resources.
	 *
	 * @return a list of all resources.
	 */
	List<T> findAll();

	/**
	 * Find all resources (pageable).
	 *
	 * @param pageRequest
	 *            page request
	 * @return resources
	 */
	/**
	 * Find all resources (pageable).
	 *
	 * @param pageRequest
	 *            page request
	 * @return resources
	 */
	Page<T> findAll(Pageable pageRequest);

	public Page<T> findPaginated(Integer pageNumber, Integer pageSize, String direction, String properties);

	/**
	 * Count all resources.
	 *
	 * @return number of resources
	 */
	Long count();

  /**
   * Returns a single entity matching the given {@link Example}.
   * 
   * @param example
   * @return
   */
  T findOne(T example);

  /**
   * Returns all entities matching the given {@link Example}.
   * 
   * @param example
   * @return
   */
  List<T> findAll(T example);

  /**
   * Returns a {@link Page} of entities matching the given {@link Example}.
   * 
   * @param example
   * @param pageable
   * @return
   */
  Page<T> findAll(T example, Pageable pageable);

  /**
   * Returns all entities matching the given {@link Example} and {@link Sort}.
   * 
   * @param example
   * @param sort
   * @return
   */
  List<T> findAll(T example, Sort sort);

  /**
   * Returns the number of instances that the given {@link Example} will return.
   * 
   * @param example the {@link Example} to count instances for
   * @return the number of instances
   */
  long count(T example);
  /**
   * Returns a {@link Page} of entities matching the given {@link Example} and {@link Sort}.
   * 
   * @param example
   * @param pageable
   * @param sort
   * @return
   */
  Page<T> findAll(T example, Pageable pageable, Sort sort);
  
  public Page<T> findPaginated(T example, Integer pageNumber, Integer pageSize, String direction, String properties);
}
