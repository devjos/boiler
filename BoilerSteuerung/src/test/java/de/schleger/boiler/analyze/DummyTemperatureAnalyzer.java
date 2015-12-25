package de.schleger.boiler.analyze;

import de.schleger.boiler.analyze.TemperatureAnalyzer;
import de.schleger.boiler.temperature.Temperature;

public class DummyTemperatureAnalyzer implements TemperatureAnalyzer {

	private Temperature t;

	@Override
	public Temperature getTemperature() 
	{
		return t;
	}
	
	public void setTemperature(Temperature t)
	{
		this.t = t;		
	}
}
