package org.fuc.core;

import org.fuc.core.criterias.EmptyCriteria;

public abstract class Criteria {
    private static Criteria empty = null;

    public static Criteria empty() {
        if (empty == null) {
            empty = new EmptyCriteria();
        }
        return empty;
    }
}
