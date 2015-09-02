package org.fuc.core.criterias;

import org.fuc.core.Criteria;
import org.fuc.entities.City;

public class CityCriteria extends Criteria {
    private City city;

    public CityCriteria(City city) {
        this.city = city;
    }

    public City getCity() {
        return city;
    }
}
