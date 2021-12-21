package de.schleger.boiler.heat;

import de.schleger.boiler.config.HeatPower;

public class DummyHeatTimeInterpolator implements HeatTimeInterpolator {
    @Override
    public Float interpolateTimeToHeat(float deltaTemperature, HeatPower heatPower) {
        switch (heatPower) {
        case HEAT_POWER_3:
            return deltaTemperature * 2;
        case HEAT_POWER_2:
            return deltaTemperature * 4;
        case HEAT_POWER_1:
            return deltaTemperature * 6;
        default:
            return 0f;
        }
    }
}
