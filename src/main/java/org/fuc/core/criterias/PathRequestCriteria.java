package org.fuc.core.criterias;

import org.fuc.entities.Account;
import org.fuc.entities.Ride;

import java.util.List;

public class PathRequestCriteria extends AccountCriteria {
    private List<Ride> rides;

    public PathRequestCriteria(Account account, List<Ride> rides) {
        super(account);
        this.rides = rides;
    }

    public List<Ride> getRides() {
        return rides;
    }
}
