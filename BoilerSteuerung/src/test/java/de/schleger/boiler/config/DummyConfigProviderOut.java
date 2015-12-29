package de.schleger.boiler.config;

public class DummyConfigProviderOut implements ConfigProviderOut 
{
	private HeatPower heatPower;
	private int fillLevel;

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

	@Override
	public void setFillLevel(int fillLevel) 
	{
		this.fillLevel = fillLevel;
	}

	@Override
	public int getFillLevel() 
	{
		return fillLevel;
	}
}
