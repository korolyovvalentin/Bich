package org.fuc.core.criterias;

import org.fuc.core.Criteria;

public class IdCriteria extends Criteria {
    private Long id;

    public IdCriteria(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
