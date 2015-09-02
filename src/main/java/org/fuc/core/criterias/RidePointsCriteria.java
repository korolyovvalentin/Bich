package org.fuc.core.criterias;

import org.fuc.entities.City;
import org.fuc.entities.Ride;

public class RidePointsCriteria {
    private Ride ride;
    private City[] cities;

    public RidePointsCriteria(Ride ride, City[] cities) {
        this.ride = ride;
        this.cities = cities;
    }

    public Ride getRide() {
        return ride;
    }

    public City[] getCities() {
        return cities;
    }
}
