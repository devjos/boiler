package de.schleger.boiler.filter;

import java.util.Date;

import de.schleger.boiler.analyze.TemperatureAnalyzer;
import de.schleger.boiler.config.ConfigProvider;
import de.schleger.boiler.config.HeatPower;
import de.schleger.boiler.heat.HeatProvider;
import de.schleger.boiler.heat.HeatTimeCalculator;
import de.schleger.boiler.temperature.Temperature;
import de.schleger.boiler.time.TimeProvider;

public class TemperatureActionFilterNachtHeizung implements TemperatureActionFilter {

	private TimeProvider timeProvider;
	private ConfigProvider configProvider;
	private TemperatureAnalyzer temperatureAnalyzer;
	private HeatProvider heatProvider;
	private HeatTimeCalculator heatTimeCalculator;

	public TemperatureActionFilterNachtHeizung(TimeProvider timeProvider, ConfigProvider configProvider, TemperatureAnalyzer temperatureAnalyzer, HeatProvider heatProvider, HeatTimeCalculator heatTimeCalculator) 
	{
		this.timeProvider = timeProvider;
		this.configProvider = configProvider;
		this.temperatureAnalyzer = temperatureAnalyzer;
		this.heatProvider = heatProvider;
		this.heatTimeCalculator = heatTimeCalculator;
	}
	
	@Override
	public boolean filter() 
	{	
		if(!timeProvider.isNight())
		{
			heatProvider.heat(HeatPower.HEAT_POWER_0);
			return false;
		}
		
		Temperature temperature = temperatureAnalyzer.getTemperature();
		Temperature targetTemperature = configProvider.getTargetTemperature();
				
		int timeInMinutes = heatTimeCalculator.calculate(temperature, targetTemperature, HeatPower.HEAT_POWER_3);
		
		if(timeInMinutes <= 0)
		{
			heatProvider.heat(HeatPower.HEAT_POWER_0);
			return false;
		}
		
		Date calculatedEndTime = timeProvider.addMinutesToTime(timeInMinutes);
		Date endTime = timeProvider.getNextNachtheizungEndTime();
		
		if(calculatedEndTime.after(endTime))
		{
			heatProvider.heat(HeatPower.HEAT_POWER_3);
			return true;
		}
		else
		{
			heatProvider.heat(HeatPower.HEAT_POWER_0);
			return false;
		}
	}
}
