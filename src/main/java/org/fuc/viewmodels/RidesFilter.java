package org.fuc.viewmodels;

import java.util.Date;

public class RidesFilter {
    private String start;
    private String end;
    private Date date;

    public RidesFilter() {
    }

    public RidesFilter(String start, String end, Date date) {
        this.start = start;
        this.end = end;
        this.date = date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
