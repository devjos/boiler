package de.schleger.boiler.filter;

import java.util.Date;

import de.schleger.boiler.analyze.TemperatureAnalyzer;
import de.schleger.boiler.config.ConfigProviderIn;
import de.schleger.boiler.config.ConfigProviderOut;
import de.schleger.boiler.config.HeatPower;
import de.schleger.boiler.heat.HeatTimeCalculator;
import de.schleger.boiler.temperature.Temperature;
import de.schleger.boiler.time.TimeProvider;

public class TemperatureActionFilterNachtHeizung implements TemperatureActionFilter {

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
		
		if(timeInMinutes <= 0)
		{
			configProviderOut.setHeatPower(HeatPower.HEAT_POWER_0);
			return false;
		}
		
		Date calculatedEndTime = timeProvider.addMinutesToTime(timeInMinutes);
		Date endTime = timeProvider.getNextNachtheizungEndTime();
		
		if(calculatedEndTime.after(endTime))
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
