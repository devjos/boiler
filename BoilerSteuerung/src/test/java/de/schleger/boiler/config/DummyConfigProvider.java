package de.schleger.boiler.config;

import de.schleger.boiler.temperature.Temperature;

public class DummyConfigProvider implements ConfigProvider 
{
	private Temperature targetTemperature;

	@Override
	public Temperature getTargetTemperature() 
	{
		return targetTemperature;
	}
	
	public void setTargetTemperature(Temperature targetTemperature)
	{
		this.targetTemperature = targetTemperature;		
	}	
}
