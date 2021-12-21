package de.schleger.boiler.task;

import java.util.TimerTask;

import de.schleger.boiler.schedule.BoilerSchedule;

public class BoilerTaskImpl extends TimerTask {
    private BoilerSchedule logic;

    public BoilerTaskImpl(BoilerSchedule logic) {
        this.logic = logic;
    }

    @Override
    public void run() {
        logic.analyse();
    }
}
