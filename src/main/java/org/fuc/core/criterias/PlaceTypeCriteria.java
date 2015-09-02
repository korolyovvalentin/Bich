package org.fuc.core.criterias;

import org.fuc.entities.City;

public class PlaceTypeCriteria extends CityCriteria {
    private String type;

    public PlaceTypeCriteria(City city, String type) {
        super(city);

        this.type = type;
    }

    public String getType() {
        return type;
    }
}
