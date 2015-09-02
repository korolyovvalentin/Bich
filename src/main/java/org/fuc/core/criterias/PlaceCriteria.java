package org.fuc.core.criterias;

import org.fuc.core.Criteria;
import org.fuc.entities.Place;

public class PlaceCriteria extends Criteria {
    private Place place;

    public PlaceCriteria(Place place) {
        this.place = place;
    }

    public Place getPlace() {
        return place;
    }
}
