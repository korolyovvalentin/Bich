package org.fuc.core.criterias;

import org.fuc.core.Criteria;
import org.fuc.entities.City;

public class PathCriteria extends Criteria {
    private City start;
    private City end;

    public PathCriteria() {
    }

    public PathCriteria(City start, City end) {
        this.start = start;
        this.end = end;
    }

    public City getStart() {
        return start;
    }

    public City getEnd() {
        return end;
    }
}
