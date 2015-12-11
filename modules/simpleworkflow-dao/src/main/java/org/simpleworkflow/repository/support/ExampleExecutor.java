package org.simpleworkflow.repository.support;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public interface ExampleExecutor<T> {

  /**
   * Returns a single entity matching the given {@link Specification}.
   * 
   * @param example
   * @return
   */
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
  /**
   * Returns all entities matching the given {@link Specification} and {@link Sort}.
   * 
   * @param example
   * @param pageable
   * @param sort
   * @return
   */
  Page<T> findAll(T example, Pageable pageable, Sort sort);
  

}

