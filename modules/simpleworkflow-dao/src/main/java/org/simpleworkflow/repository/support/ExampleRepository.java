package org.simpleworkflow.repository.support;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.Criteria;
import org.hibernate.NullPrecedence;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.FixedExample;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

public class ExampleRepository<T> extends SimpleJpaRepository<T, Long> implements ExampleCrudRepository<T, Long> {

  private final EntityManager em;

  public ExampleRepository(JpaEntityInformation<T, ? > entityInformation, EntityManager entityManager) {
    super(entityInformation, entityManager);
    em = entityManager;
  }

  @Override
  public List<T> findAll(T example) {
    return findByExample(example, null);
  }

  @Override
  public T findOne(T example) {
    List<T> list = findByExample(example, null);
    if (list.isEmpty()) {
      return null;
    } else {
      return list.get(0);
    }
  }

  @Override
  public List<T> findAll(T example, Sort sort) {
    return findByExample(example, sort);
  }

  @Override
  public Page<T> findAll(T example, Pageable pageable, Sort sort) {

    return pageByExample(example, pageable, sort);
  }

  @Override
  public Page<T> findAll(T example, Pageable pageable) {
    return pageByExample(example, pageable, null);
  }

  @Override
  public long count(T example) {
    return findByExample(example, null).size();
  }

  @SuppressWarnings("unchecked")
  private List<T> findByExample(Object example, Sort sort) {
    DetachedCriteria detachedCriteria = createDetachedCriteriaByExample(example, sort);
    addSort(sort, detachedCriteria);
    Session session = (Session) em.getDelegate();
    Criteria criteria = detachedCriteria.getExecutableCriteria(session);

    return criteria.list();
  }

  @SuppressWarnings("unchecked")
  private Page<T> pageByExample(T example, Pageable pageable, Sort sort) {
    DetachedCriteria detachedCriteria = createDetachedCriteriaByExample(example, sort);
    Session session = (Session) em.getDelegate();

    int total = ((Number) detachedCriteria.getExecutableCriteria(session).setProjection(Projections.rowCount()).uniqueResult()).intValue();
    detachedCriteria.setResultTransformer(Criteria.ROOT_ENTITY);
    detachedCriteria.setProjection(null);

    addSort(sort, detachedCriteria);

    List<T> resultList = detachedCriteria.getExecutableCriteria(session).setFirstResult(pageable.getOffset()).setMaxResults(pageable.getPageSize()).list();
    List<T> content = total > pageable.getOffset() ? resultList : Collections.<T> emptyList();

    return new PageImpl<T>(content, pageable, total);
  }
  private DetachedCriteria createDetachedCriteriaByExample(Object example, Sort sort) {
    Assert.notNull(example, "The example must not be null!");
    Class< ? > clazz = example.getClass();
    DetachedCriteria detachedCriteria = DetachedCriteria.forClass(clazz, clazz.getName());
    detachedCriteria.add(FixedExample.create(example).enableLike().ignoreCase());

    Field[] fields = example.getClass().getDeclaredFields();
    for (Field field : fields) {
      if (field.isAnnotationPresent(OneToOne.class) || field.isAnnotationPresent(ManyToOne.class)) {
        ReflectionUtils.makeAccessible(field);
        Object value = ReflectionUtils.getField(field, example);
        if (value != null) {
          detachedCriteria.createCriteria(field.getName()).add(FixedExample.create(value).enableLike().ignoreCase());
        }
      }
    }
    return detachedCriteria;
  }

  private void addSort(Sort sort, DetachedCriteria detachedCriteria) {
    if (sort != null) {
      List<org.hibernate.criterion.Order> orders = toOrders(sort);
      for (org.hibernate.criterion.Order order : orders) {
        detachedCriteria.addOrder(order);
      }
    }
  }
  public static List<org.hibernate.criterion.Order> toOrders(Sort sort) {

    List<org.hibernate.criterion.Order> orders = new ArrayList<org.hibernate.criterion.Order>();

    if (sort == null) {
      return orders;
    }

    for (org.springframework.data.domain.Sort.Order order : sort) {
      orders.add(toHibernateOrder(order));
    }

    return orders;
  }

  private static Order toHibernateOrder(org.springframework.data.domain.Sort.Order order) {
    org.hibernate.criterion.Order result;
    // direction
    if (order.getDirection().equals(Direction.ASC)) {
      result = org.hibernate.criterion.Order.asc(order.getProperty());
    } else {
      result = org.hibernate.criterion.Order.desc(order.getProperty());
    }
    // null handling
    switch (order.getNullHandling()) {
    case NATIVE:
      result = result.nulls(NullPrecedence.NONE);
      break;
    case NULLS_FIRST:
      result = result.nulls(NullPrecedence.FIRST);
      break;
    case NULLS_LAST:
      result = result.nulls(NullPrecedence.LAST);
      break;
    default:
      break;
    }
    // ignoreCase
    if (order.isIgnoreCase()) {
      result.ignoreCase();
    }

    return result;
  }
}