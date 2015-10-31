package org.fuc.core.criterias;

import org.fuc.core.Criteria;
import org.fuc.entities.Account;
import org.fuc.entities.Ride;

public class RideCriteria extends AccountCriteria {
    private Ride ride;

    public RideCriteria(Ride ride, Account account) {
        super(account);
        this.ride = ride;
    }

    public Ride getRide() {
        return ride;
    }
}
