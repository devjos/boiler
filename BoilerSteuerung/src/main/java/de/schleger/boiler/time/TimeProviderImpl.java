package de.schleger.boiler.time;

import java.time.LocalDateTime;

public class TimeProviderImpl implements TimeProvider {
    private LocalDateTimeProvider dateTimeProvider = LocalDateTime::now;
    private LocalDateTime dateTime;

    @Override
    public LocalDateTime getTime() {
        return dateTime;
    }

    /**
     * Zwischen 22Uhr und 6Uhr wird als Nacht ausgegeben
     */
    @Override
    public boolean isNight() {
        return BoilerTime.isNight(getTime());
    }

    /**
     * Ist immer das nächste 6 Uhr morgens
     */
    @Override
    public LocalDateTime getNextNachtheizungEndTime() {
        LocalDateTime time = getTime();

        // Wenn größer 6 dann ist es der nächste Tag
        if (time.getHour() >= 6) {
            time = time.plusDays(1);
        }

        LocalDateTime dateTime = LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(), 6, 0);

        return dateTime;
    }

    /**
     * Der erste Tag des nächsten Monats 6 Uhr morgens
     */
    @Override
    public LocalDateTime getNextLegionellenEndTime() {
        LocalDateTime time = getTime();

        // Wenn größer 6 dann ist es der nächste Tag
        if (time.getDayOfMonth() >= 2) {
            time = time.plusMonths(1);
        } else if (time.getHour() >= 6) {
            time = time.plusMonths(1);
        }

        LocalDateTime dateTime = LocalDateTime.of(time.getYear(), time.getMonth(), 1, 6, 0);

        return dateTime;
    }

    @Override
    public LocalDateTime addMinutesToTime(int minutes) {
        return getTime().plusMinutes(minutes);
    }

    public void setDateTimeProvider(LocalDateTimeProvider prov) {
        this.dateTimeProvider = prov;
        update();
    }

    @Override
    public void update() {
        dateTime = dateTimeProvider.now();
    }

}
