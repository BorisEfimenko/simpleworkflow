package org.simpleworkflow.repository;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.simpleworkflow.domain.Approve;
import org.simpleworkflow.repository.specification.PlainSpecification;
import org.simpleworkflow.repository.specification.SearchCriteria;
import org.springframework.data.jpa.domain.Specifications;

public class CriteriaTest {

  @Test
  public void findByExample() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
    Approve example = new Approve();
    example.setProcessDefinitionKey("%cess1");
    example.setProcessDefinitionVersion("1");

    List<PlainSpecification<Approve>> specList = getSpecList(example);
    Specifications<Approve> totalSpec = Specifications.where(specList.get(0));
    for (int i = 1; i < specList.size(); i++) {
      totalSpec.and(specList.get(i));
    }

  }

  @SuppressWarnings("PMD.AvoidDeeplyNestedIfStmts")
  private <T> List<PlainSpecification<T>> getSpecList(T example) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
    List<PlainSpecification<T>> specList = new ArrayList<PlainSpecification<T>>();
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
    if (specList.isEmpty()) {
      // add fake criteria (if value is null then always true)
      specList.add(new PlainSpecification<T>(new SearchCriteria("", "", null)));
    }
    return specList;
  }
}
