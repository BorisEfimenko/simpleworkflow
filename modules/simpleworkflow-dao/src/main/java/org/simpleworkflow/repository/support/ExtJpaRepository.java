package org.simpleworkflow.repository.support;

import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.FixedExample;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

public class ExtJpaRepository<T> extends SimpleJpaRepository<T, Long> implements JpaCrudRepository<T, Long> {

  private final EntityManager em;

  public ExtJpaRepository(JpaEntityInformation<T, ? > entityInformation, EntityManager entityManager) {
    super(entityInformation, entityManager);
    em = entityManager;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<T> qbe(T exampleEntity) {
    Assert.notNull(exampleEntity, "The exampleEntity must not be null!"); 
    DetachedCriteria detachedCriteria = getCriteriaByEntity(exampleEntity);
    Session session = (Session) em.getDelegate();
    return (List<T>) detachedCriteria.getExecutableCriteria(session).list();
  }
  
  private DetachedCriteria getCriteriaByEntity(Object exampleEntity) {
    Class< ? > clazz = exampleEntity.getClass();
    DetachedCriteria detachedCriteria = DetachedCriteria.forClass(clazz, clazz.getName());
    detachedCriteria.add(FixedExample.create(exampleEntity).enableLike().ignoreCase());

    Field[] fields = exampleEntity.getClass().getDeclaredFields();
    for (Field field : fields) {
      if (field.isAnnotationPresent(OneToOne.class) || field.isAnnotationPresent(ManyToOne.class)) {
        ReflectionUtils.makeAccessible(field);
        Object value = ReflectionUtils.getField(field, exampleEntity);
        if (value != null) {
         detachedCriteria.createCriteria(field.getName()).add(FixedExample.create(value).enableLike().ignoreCase());
        }
      }
    }
    return detachedCriteria;
  }
}