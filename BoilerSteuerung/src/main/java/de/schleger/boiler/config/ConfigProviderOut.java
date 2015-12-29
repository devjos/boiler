package de.schleger.boiler.config;

public interface ConfigProviderOut 
{
	public void setHeatPower(HeatPower heatPower);
	
	public HeatPower isHeating();
	
	public void setFillLevel(int fillLevel);
	
	public int getFillLevel();
}
