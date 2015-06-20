package org.fuc.viewmodels.Rides;

import org.fuc.entities.City;
import org.fuc.entities.RidePoint;
import org.hibernate.validator.constraints.Range;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class RideVm {
    private Long id;
    private City departure;
    private City arrival;
    private List<City> cities = new LinkedList<>();
    private Date date;
    @Range(min = 0, message = "Must be positive number")
    private Integer maxParticipants;
    private int requestsCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public int getRequestsCount() {
        return requestsCount;
    }

    public void setRequestsCount(int requestsCount) {
        this.requestsCount = requestsCount;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }
}
