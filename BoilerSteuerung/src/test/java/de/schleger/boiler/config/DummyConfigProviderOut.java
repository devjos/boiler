package de.schleger.boiler.config;

import de.schleger.boiler.config.ConfigProviderOut;
import de.schleger.boiler.config.HeatPower;

public class DummyConfigProviderOut implements ConfigProviderOut 
{
	private HeatPower heatPower;

	@Override
	public void setHeatPower(HeatPower heatPower) 
	{
		this.heatPower = heatPower;

	}

	@Override
	public HeatPower isHeating() 
	{
		return heatPower;
	}
}
