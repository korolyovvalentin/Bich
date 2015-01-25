package org.fuc.viewmodels.Rides;

import org.fuc.entities.City;
import org.hibernate.validator.constraints.Range;

import java.util.Date;

public class RideVm {
    private City departure;
    private City arrival;
    private Date date;
    @Range(min = 0, message = "Must be positive number")
    private Integer maxParticipants;

    public City getDeparture() {
        return departure;
    }

    public void setDeparture(City departure) {
        this.departure = departure;
    }

    public City getArrival() {
        return arrival;
    }

    public void setArrival(City arrival) {
        this.arrival = arrival;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }
}
