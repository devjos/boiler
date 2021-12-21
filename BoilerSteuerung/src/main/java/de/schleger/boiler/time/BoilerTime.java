package de.schleger.boiler.time;

import java.time.LocalDateTime;

public final class BoilerTime {

    private BoilerTime() {
    }

    public static boolean isNight(LocalDateTime time) {
        int hour = time.getHour();
        return 21 < hour || 6 > hour;
    }

}
