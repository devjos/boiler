package de.schleger.boiler.config;

import de.schleger.boiler.information.InformationUpdater;
import de.schleger.boiler.temperature.Temperature;

public interface ConfigProviderIn extends InformationUpdater
{
	public Temperature getTargetTemperature();
	
	public Temperature getLegionellenTemperature();

	public Temperature getEmptyTemperature();
}
