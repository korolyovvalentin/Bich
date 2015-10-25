package org.fuc.util;

import java.util.Date;

public class DateComparer {
    public static int compare(Date d1, Date d2){
        return new Date(d1.getYear(), d1.getMonth(), d1.getDay())
                .compareTo(
                        new Date(d2.getYear(), d2.getMonth(), d2.getDay())
                );
    }
}
