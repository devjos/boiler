package de.schleger.boiler.heat;

import de.schleger.boiler.config.HeatPower;
import de.schleger.boiler.temperature.Temperature;

public class HeatTimeCalulatorImpl implements HeatTimeCalculator
{	
	// 10 Min Buffer
	private static final float HEAT_BUFFER = 10.0f;
	
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
		
		// Aufrunden + Puffer
		return (int)Math.ceil(timeToHeat + HEAT_BUFFER);
	}
}
