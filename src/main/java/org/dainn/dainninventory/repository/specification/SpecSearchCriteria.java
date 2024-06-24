package org.dainn.dainninventory.repository.specification;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SpecSearchCriteria {
    private String key;
    private SearchOperation operation;
    private Object value;
    private List<Object> values;
    private boolean orPredicate;

    public SpecSearchCriteria(String key, SearchOperation operation, Object value, boolean orPredicate) {
        this.key = key;
        this.operation = operation;
        this.value = value;
        this.orPredicate = orPredicate;
    }
    public SpecSearchCriteria(String key, SearchOperation operation, List<Object> values, boolean orPredicate) {
        this.key = key;
        this.operation = operation;
        this.orPredicate = orPredicate;
        this.values = values;
    }
}
