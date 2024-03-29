package de.schleger.boiler.filter;

import java.time.LocalDateTime;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.schleger.boiler.analyze.TemperatureAnalyzer;
import de.schleger.boiler.config.HeatPower;
import de.schleger.boiler.heat.HeatTimeCalculator;
import de.schleger.boiler.model.BoilerModel;
import de.schleger.boiler.temperature.Temperature;
import de.schleger.boiler.time.TimeProvider;

public abstract class AbstractTemperaturActionFilterNachtHeizung implements TemperatureActionFilter {
    private static final Logger LOG = LogManager.getLogger(AbstractTemperaturActionFilterNachtHeizung.class);

    private TimeProvider timeProvider;
    private TemperatureAnalyzer temperatureAnalyzer;
    private HeatTimeCalculator heatTimeCalculator;
    private BoilerModel boilerModel;

    public AbstractTemperaturActionFilterNachtHeizung(TimeProvider timeProvider, BoilerModel boilerModel,
            TemperatureAnalyzer temperatureAnalyzer, HeatTimeCalculator heatTimeCalculator) {
        this.timeProvider = timeProvider;
        this.boilerModel = boilerModel;
        this.temperatureAnalyzer = temperatureAnalyzer;
        this.heatTimeCalculator = heatTimeCalculator;
    }

    abstract Temperature getTargetTemperature();

    abstract LocalDateTime getEndTime();

    @Override
    public boolean filter() {
        // Wenn noch nicht Nacht ist oder Ende der Heizzeit in weiter Ferne dann gleich wieder raus
        if (!timeProvider.isNight() || timeProvider.getTime().plusHours(12).isBefore(getEndTime())) {
            boilerModel.setHeatPower(HeatPower.HEAT_POWER_0);
            return false;
        }

        Temperature temperature = temperatureAnalyzer.getAverageTemperature();
        Temperature targetTemperature = getTargetTemperature();

        int timeInMinutes = heatTimeCalculator.calculate(temperature, targetTemperature, HeatPower.HEAT_POWER_3);

        LOG.log(Level.DEBUG, "istTemperature:" + temperature.getTemperature() + " targetTemperature:"
                + targetTemperature.getTemperature() + " heatTimeInMinutes:" + timeInMinutes);

        if (timeInMinutes <= 0) {
            boilerModel.setHeatPower(HeatPower.HEAT_POWER_0);
            return false;
        }

        LocalDateTime calculatedEndTime = timeProvider.addMinutesToTime(timeInMinutes);
        LocalDateTime endTime = getEndTime();

        LOG.log(Level.DEBUG, "endTime:" + endTime + " calculatedEndTime:" + calculatedEndTime);

        if (calculatedEndTime.isAfter(endTime)) {
            boilerModel.setHeatPower(HeatPower.HEAT_POWER_3);
            return true;
        } else {
            boilerModel.setHeatPower(HeatPower.HEAT_POWER_0);
            return false;
        }
    }
}
