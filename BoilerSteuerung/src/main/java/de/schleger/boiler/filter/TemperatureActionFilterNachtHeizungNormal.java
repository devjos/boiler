package de.schleger.boiler.filter;

import java.time.LocalDateTime;

import de.schleger.boiler.analyze.TemperatureAnalyzer;
import de.schleger.boiler.config.ConfigProviderIn;
import de.schleger.boiler.heat.HeatTimeCalculator;
import de.schleger.boiler.model.BoilerModel;
import de.schleger.boiler.temperature.Temperature;
import de.schleger.boiler.time.TimeProvider;

public class TemperatureActionFilterNachtHeizungNormal extends AbstractTemperaturActionFilterNachtHeizung {
    private TimeProvider timeProvider;
    private ConfigProviderIn configProviderIn;

    public TemperatureActionFilterNachtHeizungNormal(TimeProvider timeProvider, ConfigProviderIn configProviderIn,
            TemperatureAnalyzer temperatureAnalyzer, BoilerModel boilerModel, HeatTimeCalculator heatTimeCalculator) {
        super(timeProvider, boilerModel, temperatureAnalyzer, heatTimeCalculator);
        this.timeProvider = timeProvider;
        this.configProviderIn = configProviderIn;
    }

    @Override
    Temperature getTargetTemperature() {
        return configProviderIn.getTargetTemperature();
    }

    @Override
    LocalDateTime getEndTime() {
        return timeProvider.getNextNachtheizungEndTime();
    }
}
