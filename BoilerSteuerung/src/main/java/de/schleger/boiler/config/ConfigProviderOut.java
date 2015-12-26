package de.schleger.boiler.config;

public interface ConfigProviderOut 
{
	void setHeatPower(HeatPower heatPower);
	
	HeatPower isHeating();
}
