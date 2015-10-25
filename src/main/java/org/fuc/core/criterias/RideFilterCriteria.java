package org.fuc.core.criterias;

import org.fuc.core.Criteria;
import org.fuc.entities.City;

import java.util.Date;

public class RideFilterCriteria extends Criteria {
    private Date date;

    public RideFilterCriteria(Date date) {
        this.date = date;
    }

    public RideFilterCriteria() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
