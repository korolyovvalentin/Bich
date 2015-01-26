package org.fuc.entities;

public class RequestStatus {
    public static final String NEW = "New";
    public static final String APPROVED = "Approved";
    public static final String DECLINED = "Declined";

    public static String getStatus(String parameter) {
        switch (parameter) {
            case "New":
                return NEW;
            case "Approved":
                return APPROVED;
            case "Declined":
                return DECLINED;
        }
        throw new IllegalArgumentException(String.format("No status with name %s", parameter));
    }
}
