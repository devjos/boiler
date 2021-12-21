package de.schleger.boiler.analyze;

import de.schleger.boiler.temperature.Temperature;

public class DummyTemperatureAnalyzer implements TemperatureAnalyzer {

	private Temperature t;

	@Override
	public Temperature getAverageTemperature() 
	{
		return t;
	}
	
	public void setTemperature(Temperature t)
	{
		this.t = t;		
	}

	@Override
	public void update() {
		
	}
}
