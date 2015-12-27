package de.schleger.boiler.config;

import de.schleger.boiler.temperature.Temperature;

public interface ConfigProviderIn 
{
	public Temperature getTargetTemperature();
	
	public Temperature getLegionellenTemperature();
}
