package de.schleger.boiler.heat;

import java.time.LocalDateTime;

import de.schleger.boiler.config.HeatPower;

public interface HeatCostCalculator 
{
	public float calculate(LocalDateTime start, LocalDateTime end, HeatPower heatPower);
}
