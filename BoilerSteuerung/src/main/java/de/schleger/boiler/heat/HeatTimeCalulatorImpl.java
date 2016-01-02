package de.schleger.boiler.heat;

import de.schleger.boiler.config.HeatPower;
import de.schleger.boiler.temperature.Temperature;

public class HeatTimeCalulatorImpl implements HeatTimeCalculator
{	
	// 15 Prozent Puffer
	private static final float HEAT_BUFFER = 0.15f;
	
	private HeatTimeInterpolator heatTimeInterpolator;
	
	public HeatTimeCalulatorImpl(HeatTimeInterpolator heatTimeInterpolator) 
	{
		this.heatTimeInterpolator = heatTimeInterpolator;
	}
	
	@Override
	public int calculate(Temperature startTemp, Temperature endTemp, HeatPower heatPower)
	{		
		if(startTemp.compareTo(endTemp) >= 0)
		{
			return 0;
		}
		
		// Temperature Delta ermitteln
		float deltaTemperature = endTemp.getTemperature().floatValue() - startTemp.getTemperature().floatValue();
		// Zeit zum heizen ermitteln sowie Puffer
		float timeToHeat = heatTimeInterpolator.interpolateTimeToHeat(deltaTemperature, heatPower);
		float buffer = timeToHeat * HEAT_BUFFER;
		
		// Aufrunden + Puffer
		return (int)Math.ceil(timeToHeat + buffer);
	}
}
