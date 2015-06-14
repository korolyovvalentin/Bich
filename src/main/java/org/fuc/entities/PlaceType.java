package org.fuc.entities;

public enum PlaceType {
    Job("type.job"),
    Shelter("type.shelter");

    private String value;

    private PlaceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return name();
    }
}
