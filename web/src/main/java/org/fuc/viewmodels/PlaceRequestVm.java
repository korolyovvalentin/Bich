package org.fuc.viewmodels;

import org.fuc.entities.Place;

import java.util.Date;

public class PlaceRequestVm {
    private Date fromDate;

    private Date toDate;

    private Place place;

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}
