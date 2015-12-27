package de.schleger.boiler.filter;

import java.time.LocalDateTime;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.schleger.boiler.analyze.TemperatureAnalyzer;
import de.schleger.boiler.config.ConfigProviderIn;
import de.schleger.boiler.config.ConfigProviderOut;
import de.schleger.boiler.config.HeatPower;
import de.schleger.boiler.heat.HeatTimeCalculator;
import de.schleger.boiler.temperature.Temperature;
import de.schleger.boiler.time.TimeProvider;

public class TemperatureActionFilterNachtHeizung implements TemperatureActionFilter {

	private static final Logger LOG = LogManager.getLogger(TemperatureActionFilterNachtHeizung.class);
	
	private TimeProvider timeProvider;
	private ConfigProviderIn configProviderIn;
	private TemperatureAnalyzer temperatureAnalyzer;
	private ConfigProviderOut configProviderOut;
	private HeatTimeCalculator heatTimeCalculator;

	public TemperatureActionFilterNachtHeizung(TimeProvider timeProvider, ConfigProviderIn configProviderIn, TemperatureAnalyzer temperatureAnalyzer, ConfigProviderOut configProviderOut, HeatTimeCalculator heatTimeCalculator) 
	{
		this.timeProvider = timeProvider;
		this.configProviderIn = configProviderIn;
		this.temperatureAnalyzer = temperatureAnalyzer;
		this.configProviderOut = configProviderOut;
		this.heatTimeCalculator = heatTimeCalculator;
	}
	
	@Override
	public boolean filter() 
	{	
		if(!timeProvider.isNight())
		{
			configProviderOut.setHeatPower(HeatPower.HEAT_POWER_0);
			return false;
		}
		
		Temperature temperature = temperatureAnalyzer.getTemperature();
		Temperature targetTemperature = configProviderIn.getTargetTemperature();
				
		int timeInMinutes = heatTimeCalculator.calculate(temperature, targetTemperature, HeatPower.HEAT_POWER_3);
		
		LOG.log(Level.DEBUG, "istTemperature:" + temperature.getTemperature() 
		+ " targetTemperature:" + targetTemperature.getTemperature() + " heatTimeInMinutes:" + timeInMinutes);
		
		if(timeInMinutes <= 0)
		{
			configProviderOut.setHeatPower(HeatPower.HEAT_POWER_0);
			return false;
		}
		
		LocalDateTime calculatedEndTime = timeProvider.addMinutesToTime(timeInMinutes);
		LocalDateTime endTime = timeProvider.getNextNachtheizungEndTime();
		
		LOG.log(Level.DEBUG, "endTime:" + endTime + " calculatedEndTime:" + calculatedEndTime);
		
		if(calculatedEndTime.isAfter(endTime))
		{
			configProviderOut.setHeatPower(HeatPower.HEAT_POWER_3);
			return true;
		}
		else
		{
			configProviderOut.setHeatPower(HeatPower.HEAT_POWER_0);
			return false;
		}
	}
}
