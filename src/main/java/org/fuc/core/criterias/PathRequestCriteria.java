package org.fuc.core.criterias;

import org.fuc.entities.Account;
import org.fuc.entities.Ride;

import java.util.List;

public class PathRequestCriteria extends AccountCriteria {
    private List<Ride> rides;
    private String start;
    private String end;

    public PathRequestCriteria(Account account, List<Ride> rides, String start, String end) {
        super(account);
        this.rides = rides;
        this.start = start;
        this.end = end;
    }

    public List<Ride> getRides() {
        return rides;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
