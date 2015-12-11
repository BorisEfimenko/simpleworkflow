package org.hibernate.criterion;

import java.io.Serializable;
import java.util.Arrays;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.LikeExpression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.NullExpression;
import org.hibernate.criterion.SimpleExpression;
import org.hibernate.engine.spi.TypedValue;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.type.Type;

public class FixedExample extends Example {

  private static final long serialVersionUID = 1L;
  private final Object exampleEntity;

  private boolean isLikeEnabled;
  private Character escapeCharacter;
  private boolean isIgnoreCaseEnabled;
  private MatchMode matchMode;

  public static FixedExample create(Object exampleEntity) {
    if (exampleEntity == null) {
      throw new NullPointerException("null example entity");
    }
    return new FixedExample(exampleEntity, NotNullPropertySelector.INSTANCE);
  }

  protected FixedExample(Object exampleEntity, PropertySelector selector) {
    super(exampleEntity, selector);
    this.exampleEntity = exampleEntity;
  }
  @Override
  public FixedExample enableLike() {
    return enableLike(MatchMode.EXACT);
  }
  @Override
  public FixedExample enableLike(MatchMode matchMode) {
    this.isLikeEnabled = true;
    this.matchMode = matchMode;
    return (FixedExample) super.enableLike(matchMode);
  }
  @Override
  public FixedExample ignoreCase() {    
    this.isIgnoreCaseEnabled = true;
    return (FixedExample) super.ignoreCase();
  }

  @Override
  public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
    TypedValue[] typedValues = super.getTypedValues(criteria, criteriaQuery);

    EntityPersister meta = criteriaQuery.getFactory().getEntityPersister(criteriaQuery.getEntityName(criteria));
    // String idName=meta.getIdentifierPropertyName();

    Serializable idValue = meta.getIdentifier(exampleEntity, null);
    if (idValue == null) {
      return typedValues;
    } else {
      Type idType = meta.getIdentifierType();
      TypedValue idTypedValue = new TypedValue(idType, idValue);
      return append(typedValues, idTypedValue);
    }
  }

  @Override
  public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) {
    String sqlString = super.toSqlString(criteria, criteriaQuery);
    EntityPersister meta = criteriaQuery.getFactory().getEntityPersister(criteriaQuery.getEntityName(criteria));
    Serializable idValue = meta.getIdentifier(exampleEntity, null);
    final StringBuilder buf = new StringBuilder();
    if (idValue == null) {
      buf.append(sqlString);
    } else {
      String idName = meta.getIdentifierPropertyName();

      buf.append('(').append(sqlString);
      appendPropertyCondition(idName, idValue, criteria, criteriaQuery, buf);
      // delete "(1=1)" ?
      buf.append(')');
    }
    return buf.toString();
  }

  // use like only if propertyValue contains widcard. If isLikeEnabled==true
  // convert propertyValue to lowercase
  @Override
  @SuppressWarnings("PMD.ConfusingTernary")
  protected void appendPropertyCondition(String propertyName, Object propertyValue, Criteria criteria, CriteriaQuery cq, StringBuilder buf) {
    final Criterion condition;
    if (propertyValue != null) {
      final boolean isString = propertyValue instanceof String;
      if (isLikeEnabled && isString && !((matchMode == MatchMode.EXACT) && !containsWildcards((String) propertyValue))) {
        condition = new LikeExpression(propertyName, (String) propertyValue, matchMode,
                escapeCharacter, isIgnoreCaseEnabled);
      } else {
        condition = new SimpleExpression(propertyName, propertyValue, "=", isIgnoreCaseEnabled && isString);
      }
    } else {
      condition = new NullExpression(propertyName);
    }

    final String conditionFragment = condition.toSqlString(criteria, cq);
    if (conditionFragment.trim().length() > 0) {
      if (buf.length() > 1) {
        buf.append(" and ");
      }
      buf.append(conditionFragment);
    }
  }
  private boolean containsWildcards(String propertyValue) {
    return propertyValue.contains("%") || propertyValue.contains("_");
  }

  private <T> T[] append(T[] arr, T element) {
    final int N = arr.length;
    T[] newArr = Arrays.copyOf(arr, N + 1);
    newArr[N] = element;
    return newArr;
  }
}
