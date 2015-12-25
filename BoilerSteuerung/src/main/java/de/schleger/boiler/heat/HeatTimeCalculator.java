package de.schleger.boiler.heat;

import de.schleger.boiler.config.HeatPower;
import de.schleger.boiler.temperature.Temperature;

public interface HeatTimeCalculator 
{
	public int calculate(Temperature startTemp, Temperature endTemp, HeatPower heatPower);
}
