package org.dainn.dainninventory.repository.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

public final class SpecificationBuilder<T> {
    public final List<SpecSearchCriteria> params;

    public SpecificationBuilder() {
        this.params = new ArrayList<>();
    }

    public SpecificationBuilder(List<SpecSearchCriteria> params) {
        this.params = params;
    }

    public Specification<T> build() {
        if (params.isEmpty()) return null;
        Specification<T> result = new BaseSpecification<T>(params.get(0));
        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate() ?
                    Specification.where(result).or(new BaseSpecification<T>(params.get(i))) :
                    Specification.where(result).and(new BaseSpecification<T>(params.get(i)));
        }
        return result;
    }

    public Specification<T> joinTableWithCondition(String tableName, List<SpecSearchCriteria> criterias) {
        return (root, query, criteriaBuilder) -> {
            Join<T, ?> join = root.join(tableName);
            return toPredicate(join, criteriaBuilder, criterias);
        };
    }

//    public SpecificationBuilder<T> with(BaseSpecification<T> spec) {
//        params.add(spec.getCriteria());
//        return this;
//    }

    public SpecificationBuilder<T> with(String key, SearchOperation operation, Object value, boolean orPredicate, List<Object> values) {
        params.add(values.isEmpty() ? new SpecSearchCriteria(key, operation, value, orPredicate)
                : new SpecSearchCriteria(key, operation, value, orPredicate, values));
        return this;
    }

    public Predicate toPredicate(Join<T, ?> root, CriteriaBuilder criteriaBuilder, List<SpecSearchCriteria> criterias) {
        List<Predicate> predicates = new ArrayList<>();
        for (SpecSearchCriteria criteria : criterias) {
            Predicate predicate = switch (criteria.getOperation()) {
                case EQUALITY -> criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
                case NEGATION -> criteriaBuilder.notEqual(root.get(criteria.getKey()), criteria.getValue());
                case GREATER_THAN ->
                        criteriaBuilder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
                case LESS_THAN -> criteriaBuilder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
                case LIKE, CONTAINS ->
                        criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
                case STARTS_WITH -> criteriaBuilder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
                case ENDS_WITH -> criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
                case IN -> criteriaBuilder.in(root.get(criteria.getKey())).value(criteria.getValues());
                default -> throw new RuntimeException("Operation not supported yet");
            };
            predicates.add(predicate);
        }

        return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
    }
}
