package de.schleger.boiler.config;

import de.schleger.boiler.information.InformationProvider;
import de.schleger.boiler.temperature.Temperature;

public interface ConfigProviderIn extends InformationProvider
{
	public Temperature getTargetTemperature();
	
	public Temperature getLegionellenTemperature();
}
