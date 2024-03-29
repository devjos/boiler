package de.schleger.boiler.time;

import java.time.LocalDateTime;

public class DummyTimeProvider implements TimeProvider {
    private boolean isNight;
    private LocalDateTime nextNachtHezungEndTime;
    private LocalDateTime time;
    private LocalDateTime dateAddMinutesToTime;
    private LocalDateTime nextLegionellenEndTime;

    @Override
    public boolean isNight() {
        return isNight;
    }

    public void setNight(boolean isNight) {
        this.isNight = isNight;
    }

    @Override
    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public LocalDateTime getNextNachtheizungEndTime() {
        return nextNachtHezungEndTime;
    }

    @Override
    public LocalDateTime addMinutesToTime(int minutes) {
        return dateAddMinutesToTime;
    }

    public void setDateAddMinutesToTime(LocalDateTime dateAddMinutesToTime) {
        this.dateAddMinutesToTime = dateAddMinutesToTime;
    }

    public void setNextNachtHezungEndTime(LocalDateTime nextNachtHezungEndTime) {
        this.nextNachtHezungEndTime = nextNachtHezungEndTime;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public void update() {
        // wird für Tests nicht gebraucht

    }

    @Override
    public LocalDateTime getNextLegionellenEndTime() {
        return nextLegionellenEndTime;
    }

    public void setNextLegionellenEndTime(LocalDateTime nextLegionellenEndTime) {
        this.nextLegionellenEndTime = nextLegionellenEndTime;
    }
}
