package org.fuc.core.criterias;

import org.fuc.entities.Account;
import org.fuc.entities.Ride;

public class RideParticipantCriteria extends AccountCriteria {
    private Ride ride;

    public RideParticipantCriteria(Account account, Ride ride) {
        super(account);
        this.ride = ride;
    }

    public Ride getRide() {
        return ride;
    }
}
