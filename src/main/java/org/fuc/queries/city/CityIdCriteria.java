package org.fuc.queries.city;

import org.fuc.core.Criteria;

public class CityIdCriteria extends Criteria {
    private Long id;

    public CityIdCriteria(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
