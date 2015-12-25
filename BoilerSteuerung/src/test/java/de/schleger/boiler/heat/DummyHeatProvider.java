package de.schleger.boiler.heat;

import de.schleger.boiler.config.HeatPower;

public class DummyHeatProvider implements HeatProvider 
{
	private HeatPower heatPower;

	@Override
	public void heat(HeatPower heatPower) 
	{
		this.heatPower = heatPower;

	}

	@Override
	public HeatPower isHeating() 
	{
		return heatPower;
	}
	
	public void setHeatPower(HeatPower heatPower) 
	{
		this.heatPower = heatPower;
	}
}
