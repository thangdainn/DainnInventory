package org.dainn.dainninventory.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class SpecificationBuilder<T> {
    public final List<SpecSearchCriteria> params;

    public SpecificationBuilder() {
        this.params = new ArrayList<>();
    }

    public Specification<T> build() {
        if (params.isEmpty()) return null;
        Specification<T> result = new BaseSpecification<T>(params.get(0));
        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate() ?
                    Specification.where(result).or(new BaseSpecification<>(params.get(i))) :
                    Specification.where(result).and(new BaseSpecification<>(params.get(i)));
        }
        return result;
    }

    public Specification<T> joinTableWithCondition(String tableName, List<SpecSearchCriteria> criterias) {
        return (root, query, criteriaBuilder) -> {
            Join<T, ?> join = root.join(tableName);
            return toPredicate(join, criteriaBuilder, criterias);
        };
    }

    public SpecificationBuilder<T> with(String key, SearchOperation operation, Object value, boolean orPredicate) {
        params.add(new SpecSearchCriteria(key, operation, value, orPredicate));
        return this;
    }
    public SpecificationBuilder<T> with(String key, SearchOperation operation, List<Object> values, boolean orPredicate) {
        params.add(new SpecSearchCriteria(key, operation, values, orPredicate));
        return this;
    }

    public Predicate toPredicate(Join<T, ?> root, CriteriaBuilder criteriaBuilder, List<SpecSearchCriteria> criterias) {
        List<Predicate> predicates = new ArrayList<>();
        for (SpecSearchCriteria criteria : criterias) {
            Predicate predicate = switch (criteria.getOperation()) {
                case EQUALITY -> criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
                case NEGATION -> criteriaBuilder.notEqual(root.get(criteria.getKey()), criteria.getValue());
                case GREATER_THAN_OR_EQUAL ->
                        criteriaBuilder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
                case LESS_THAN_OR_EQUAL ->
                        criteriaBuilder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
                case LIKE, CONTAINS ->
                        criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
                case STARTS_WITH -> criteriaBuilder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
                case ENDS_WITH -> criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
//                case IN -> criteriaBuilder.in(root.get(criteria.getKey())).value(criteria.getValues());
                case IN -> {
                    CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get(criteria.getKey()));
                    List<Object> values = criteria.getValues();
                    for (Object value : values) {
                        in.value(value);
                    }
                    yield in;
                }
            };
            predicates.add(predicate);
        }
        return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
    }
}
