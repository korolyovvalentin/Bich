package org.fuc.core.criterias;

import org.fuc.core.Criteria;
import org.fuc.entities.Ride;

public class RideCriteria extends Criteria {
    private Ride ride;

    public RideCriteria(Ride ride) {
        this.ride = ride;
    }

    public Ride getRide() {
        return ride;
    }
}
