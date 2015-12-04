package org.simpleworkflow.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class PlainSpecification<T> implements Specification<T> {

	private final SearchCriteria criteria;

	public PlainSpecification(SearchCriteria criteria) {
		this.criteria = criteria;
	}

	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) {
		if ((criteria.getValue() == null) || criteria.getValue().toString().equals("")) {
			// if value is null then always true
			return cb.and();
		} else if (criteria.getOperation().equalsIgnoreCase("=")) {
			return cb.equal(root.<String> get(criteria.getKey()), criteria
					.getValue().toString());
		} else if (criteria.getOperation().equalsIgnoreCase(">")) {
			return cb.greaterThanOrEqualTo(
					root.<String> get(criteria.getKey()), criteria.getValue()
							.toString());
		} else if (criteria.getOperation().equalsIgnoreCase("<")) {
			return cb.lessThanOrEqualTo(root.<String> get(criteria.getKey()),
					criteria.getValue().toString());
		} else if (criteria.getOperation().equalsIgnoreCase("like")) {
			if (root.get(criteria.getKey()).getJavaType() == String.class) {
				return cb.like(root.<String> get(criteria.getKey()), criteria
						.getValue().toString());
			} else {
				return cb.equal(root.get(criteria.getKey()),
						criteria.getValue());
			}
		}
		return null;
	}
}