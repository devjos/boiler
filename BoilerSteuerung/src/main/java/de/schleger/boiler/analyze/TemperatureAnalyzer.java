package de.schleger.boiler.analyze;

import de.schleger.boiler.information.InformationProvider;
import de.schleger.boiler.temperature.Temperature;

public interface TemperatureAnalyzer extends InformationProvider
{	
	public Temperature getLastTemperature();
	
	//TODO build average over the last x minutes/hours
	public Temperature getAverageTemperature();
}
