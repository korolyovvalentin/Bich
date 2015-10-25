package org.fuc.core.model;

import org.fuc.entities.City;
import org.fuc.entities.Ride;

public class PathSegment {
    private Ride ride;
    private City start;
    private City end;

    public PathSegment() {
    }

    public PathSegment(Ride ride, City start, City end) {
        this.ride = ride;
        this.start = start;
        this.end = end;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public City getStart() {
        return start;
    }

    public void setStart(City start) {
        this.start = start;
    }

    public City getEnd() {
        return end;
    }

    public void setEnd(City end) {
        this.end = end;
    }
}
