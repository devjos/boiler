package de.schleger.boiler.heat;

import de.schleger.boiler.config.HeatPower;

public interface HeatProvider 
{
	void heat(HeatPower heatPower);
	
	HeatPower isHeating();
}
