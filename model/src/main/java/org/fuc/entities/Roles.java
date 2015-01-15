package org.fuc.entities;

public enum Roles {
    Beatnik("role.beatnik"),
    Driver("role.driver");

    private String value;

    private Roles(String value) {
        this.value = value;
    }

    public String getValue() {
        return name();
    }
}