package org.fuc.core.criterias;

import org.fuc.core.Criteria;
import org.fuc.entities.City;

public class CitiesCriteria extends Criteria{
    private City[] cities;

    public CitiesCriteria(City[] cities) {
        this.cities = cities;
    }

    public City[] getCities() {
        return cities;
    }
}
