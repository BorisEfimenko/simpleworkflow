package org.simpleworkflow.repository.specification;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

import org.simpleworkflow.domain.Approve;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

@Service
public class SpecificationConvertService {

  private final Logger log = LoggerFactory.getLogger(SpecificationConvertService.class);

  public <T> Specifications<T> getSpecByExample(T example) {
    List<PlainSpecification<T>> specList = getSpecList(example);
    Specifications<T> totalSpec = Specifications.where(specList.get(0));
    for (int i = 1; i < specList.size(); i++) {
      totalSpec=totalSpec.and(specList.get(i));
    }
    return totalSpec;
  }

  @SuppressWarnings("PMD.AvoidDeeplyNestedIfStmts")
  private <T> List<PlainSpecification<T>> getSpecList(T example) {
    List<PlainSpecification<T>> specList = new ArrayList<PlainSpecification<T>>();
    try {
      for (PropertyDescriptor pd : Introspector.getBeanInfo(Approve.class).getPropertyDescriptors()) {
        if (pd.getReadMethod() != null && !"class".equals(pd.getName())) {
          Object value = pd.getReadMethod().invoke(example);
          if (value != null) {
            String operation = "=";
            if ((value instanceof String) && ((String) value).contains("%")) {
              operation = "like";
            }
            specList.add(new PlainSpecification<T>(new SearchCriteria(pd.getName(), operation, value)));
          }
        }
      }

    } catch (Exception e) {
      log.info("Unforeseen use specification", e);
    }

    if (specList.isEmpty()) {
      // add fake criteria (if value is null then always true)
      specList.add(new PlainSpecification<T>(new SearchCriteria("", "", "")));
    }
    return specList;
  }

/*
  @SuppressWarnings("PMD.AvoidDeeplyNestedIfStmts")
  private <T> List<Specification<T>> getSpecListv2(T example) {
    List<Specification<T>> specList = new ArrayList<Specification<T>>();
    try {
      for (PropertyDescriptor pd : Introspector.getBeanInfo(Approve.class).getPropertyDescriptors()) {
        if (pd.getReadMethod() != null && !"class".equals(pd.getName())) {
          Object value = pd.getReadMethod().invoke(example);
          if (value != null) {
            if ((value instanceof String) && ((String) value).contains("%")) {
              specList.add(new LikeSpec<T>(pd.getName(),(String) value));
            } else {
              specList.add(new EqualSpec<T>(pd.getName(), value));
            }
          }
        }
      }

    } catch (Exception e) {
      log.info("Unforeseen use specification", e);
    }

    if (specList.isEmpty()) {
      // add fake criteria (if value is null then always true)
      specList.add(new NoneSpec<T>());
    }
    return specList;
  }

  class EqualSpec<T> implements Specification<T> {

    String property;
    Object value;

    public EqualSpec(String property, Object value) {
      this.property = property;
      this.value = value;
    }
    public Predicate toPredicate(Root<T> root, CriteriaQuery< ? > query, CriteriaBuilder builder) {

      return builder.equal(root.get(property), value);
    }
  }

  class LikeSpec<T> implements Specification<T> {

    String property;
    String value;

    public LikeSpec(String property, String value) {
      this.property = property;
      this.value = value;
    }
    public Predicate toPredicate(Root<T> root, CriteriaQuery< ? > query, CriteriaBuilder builder) {

      return builder.like(root.<String> get(property), value);
    }
  }

  class NoneSpec<T> implements Specification<T> {

    public Predicate toPredicate(Root<T> root, CriteriaQuery< ? > query, CriteriaBuilder builder) {

      return builder.and();
    }
  }
*/
}
