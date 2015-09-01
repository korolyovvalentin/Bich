package org.fuc.core;

public abstract class Criteria {
    private static Criteria empty = null;

    public static Criteria empty() {
        if (empty == null) {
            empty = new Criteria() {
            };
        }
        return empty;
    }
}
