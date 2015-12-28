package de.schleger.boiler.analyze;

import de.schleger.boiler.analyze.TemperatureAnalyzer;
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
	public Temperature getLastTemperature() {
		return t;
	}

	@Override
	public void updateInformation() {
		// TODO Auto-generated method stub
		
	}
}
