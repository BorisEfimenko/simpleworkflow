package org.simpleworkflow.service;

import java.util.List;
import java.util.Set;

import org.simpleworkflow.domain.AbstractEntity;
import org.simpleworkflow.repository.iface.JpaCrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public interface CrudService<T extends AbstractEntity, R extends JpaCrudRepository<T, Long>> {
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
	T findById(Long id);

	/**
	 * Find resources by their ids.
	 *
	 * @param ids
	 *            Resource ids
	 * @return a list of retrieved resources, empty if no resource found
	 */
	Iterable<T> findByIds(Set<Long> ids);

	/**
	 * Find all resources.
	 *
	 * @return a list of all resources.
	 */
	Iterable<T> findAll();

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

  T findOne(T example);

  /**
   * Returns all entities matching the given {@link Specification}.
   * 
   * @param example
   * @return
   */
  List<T> findAll(T example);

  /**
   * Returns a {@link Page} of entities matching the given {@link Specification}.
   * 
   * @param example
   * @param pageable
   * @return
   */
  Page<T> findAll(T example, Pageable pageable);

  /**
   * Returns all entities matching the given {@link Specification} and {@link Sort}.
   * 
   * @param example
   * @param sort
   * @return
   */
  List<T> findAll(T example, Sort sort);

  /**
   * Returns the number of instances that the given {@link Specification} will return.
   * 
   * @param example the {@link Specification} to count instances for
   * @return the number of instances
   */
  long count(T example);
}
