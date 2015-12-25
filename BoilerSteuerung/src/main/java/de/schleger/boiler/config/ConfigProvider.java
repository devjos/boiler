package de.schleger.boiler.config;

import de.schleger.boiler.temperature.Temperature;

public interface ConfigProvider 
{
	public Temperature getTargetTemperature();
}
