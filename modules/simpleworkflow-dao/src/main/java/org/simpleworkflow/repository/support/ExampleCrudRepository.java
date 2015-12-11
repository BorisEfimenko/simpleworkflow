package org.simpleworkflow.repository.support;

import java.io.Serializable;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface ExampleCrudRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID> , ExampleExecutor<T>{
  
  
}