package org.fuc.entities;

public class RoleProvider {
    public static final String ROLE_BEATNIK = "ROLE_BEATNIK";
    public static final String ROLE_DRIVER = "ROLE_DRIVER";

    public static String getRole(String parameter) {
        switch (parameter) {
            case "Beatnik":
                return ROLE_BEATNIK;
            case "Driver":
                return ROLE_DRIVER;
        }
        throw new IllegalArgumentException(String.format("No role with name %s", parameter));
    }
}
