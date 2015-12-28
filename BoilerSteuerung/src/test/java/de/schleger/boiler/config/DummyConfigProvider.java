package de.schleger.boiler.config;

import de.schleger.boiler.temperature.Temperature;

public class DummyConfigProvider implements ConfigProviderIn 
{
	private Temperature targetTemperature;
	private Temperature legionellenTemp;

	@Override
	public Temperature getTargetTemperature() 
	{
		return targetTemperature;
	}
	
	public void setTargetTemperature(Temperature targetTemperature)
	{
		this.targetTemperature = targetTemperature;		
	}

	@Override
	public Temperature getLegionellenTemperature() {
		return this.legionellenTemp;
	}	
	
	public void setLegionellenTemperature(Temperature temp){
		this.legionellenTemp = temp;
	}

	@Override
	public void updateInformation() {
		// TODO Auto-generated method stub
		
	}
}
