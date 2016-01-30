package de.schleger.boiler.config;

import de.schleger.boiler.temperature.Temperature;

public class DummyConfigProviderIn implements ConfigProviderIn 
{
	private Temperature targetTemperature;
	private Temperature legionellenTemp;
	private Temperature emptyTemp;
	private float priceNight;
	private float priceDay;

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
	
	public void setLegionellenTemperature(Temperature legionellenTemp){
		this.legionellenTemp = legionellenTemp;
	}
	
	@Override
	public Temperature getEmptyTemperature() 
	{
		return emptyTemp;
	}
	
	public void setEmptyTemperature(Temperature emptyTemp)
	{
		this.emptyTemp = emptyTemp;
	}	

	@Override
	public void update() {
		
	}

	@Override
	public float getEuroPerKwhNight() {
		return priceNight;
	}

	@Override
	public float getEuroPerKwhDay() {
		return priceDay;
	}
	
	public void setEuroPerKwhNight(float euro){
		this.priceNight = euro;
	}
	
	public void setEuroPerKwhDay(float euro){
		this.priceDay = euro;
	}
}
