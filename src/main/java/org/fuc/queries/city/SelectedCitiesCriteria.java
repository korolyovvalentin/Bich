package org.fuc.queries.city;

import org.fuc.core.Criteria;
import org.fuc.entities.City;

public class SelectedCitiesCriteria extends Criteria{
    private City[] cities;

    public SelectedCitiesCriteria(City[] cities) {
        this.cities = cities;
    }

    public City[] getCities() {
        return cities;
    }
}
