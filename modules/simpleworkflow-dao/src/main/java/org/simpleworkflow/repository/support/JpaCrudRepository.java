package org.simpleworkflow.repository.support;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface JpaCrudRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID> , JpaSpecificationExecutor<T>{
  
  List<T>  qbe(T example);
}