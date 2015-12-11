package org.simpleworkflow.repository.support;

import static org.springframework.data.querydsl.QueryDslUtils.QUERY_DSL_PRESENT;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.core.RepositoryMetadata;


public class ExtJpaRepositoryFactory extends JpaRepositoryFactory {

  public ExtJpaRepositoryFactory(EntityManager entityManager) {
    super(entityManager);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  protected <T, ID extends Serializable> SimpleJpaRepository<?, ?> getTargetRepository(RepositoryMetadata metadata,
      EntityManager entityManager) {

    Class<?> repositoryInterface = metadata.getRepositoryInterface();
    JpaEntityInformation<?, Serializable> entityInformation = getEntityInformation(metadata.getDomainType());

    SimpleJpaRepository<?, ?> repo = isQueryDslExecutor(repositoryInterface) ? new QueryDslJpaRepository(
        entityInformation, entityManager) : new ExtJpaRepository(entityInformation, entityManager);

    return repo;
  }

  @Override
  protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {

    if (isQueryDslExecutor(metadata.getRepositoryInterface())) {
      return QueryDslJpaRepository.class;
    } else {
      return ExtJpaRepository.class;
    }
  }


  private boolean isQueryDslExecutor(Class<?> repositoryInterface) {
    return QUERY_DSL_PRESENT && QueryDslPredicateExecutor.class.isAssignableFrom(repositoryInterface);
  }
}
