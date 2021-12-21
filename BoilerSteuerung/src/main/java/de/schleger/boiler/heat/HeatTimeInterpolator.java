package de.schleger.boiler.heat;

import de.schleger.boiler.config.HeatPower;

public interface HeatTimeInterpolator 
{
	public Float interpolateTimeToHeat(float deltaTemperature, HeatPower heatPower);
}
