package org.dainn.dainninventory.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
public class BaseSpecification<T> implements Specification<T> {
    private SpecSearchCriteria criteria;

    @Override
    public Predicate toPredicate(@NonNull Root<T> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
        return switch (criteria.getOperation()) {
            case EQUALITY -> criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
            case NEGATION -> criteriaBuilder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case GREATER_THAN_OR_EQUAL -> {
                if (criteria.getValue() instanceof Date) {
                    yield criteriaBuilder.greaterThanOrEqualTo(root.get(criteria.getKey()), (Date) criteria.getValue());
                } else if (criteria.getValue() instanceof BigDecimal) {
                    yield criteriaBuilder.greaterThanOrEqualTo(root.get(criteria.getKey()), (BigDecimal) criteria.getValue());
                } else {
                    yield criteriaBuilder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
                }
            }
            case LESS_THAN_OR_EQUAL -> {
                if (criteria.getValue() instanceof Date) {
                    yield criteriaBuilder.lessThanOrEqualTo(root.get(criteria.getKey()), (Date) criteria.getValue());
                } else if (criteria.getValue() instanceof BigDecimal) {
                    yield criteriaBuilder.lessThanOrEqualTo(root.get(criteria.getKey()), (BigDecimal) criteria.getValue());
                } else {
                    yield criteriaBuilder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
                }
            }
            case LIKE -> criteriaBuilder.like(root.get(criteria.getKey()), criteria.getValue().toString());
            case CONTAINS -> criteriaBuilder.like(criteriaBuilder.lower(
                    root.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%");
            case STARTS_WITH -> criteriaBuilder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
            case ENDS_WITH -> criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
            case IN -> {
                CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get(criteria.getKey()));
                List<Object> values = criteria.getValues();
                for (Object value : values) {
                    in.value(value);
                }
                yield in;
            }
        };
    }
}
