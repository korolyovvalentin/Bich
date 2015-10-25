package org.fuc.core.criterias;

import org.fuc.core.Criteria;

public class NameCriteria extends Criteria {
    private String name;

    public NameCriteria(String name) {
        this.name = name;
    }

    public NameCriteria() {
    }

    public String getName() {
        return name;
    }
}
