package org.fuc.entities;

public class RequestStatus {
    public static final String NEW = "New";
    public static final String APPROVED = "Approved";
    public static final String DECLINED = "Declined";
    public static final String OLD = "Old";

    public static String getStatus(String parameter) {
        switch (parameter.toLowerCase()) {
            case "new":
                return NEW;
            case "approved":
                return APPROVED;
            case "declined":
                return DECLINED;
            case "old":
                return OLD;
        }
        throw new IllegalArgumentException(String.format("No status with name %s", parameter));
    }
}
