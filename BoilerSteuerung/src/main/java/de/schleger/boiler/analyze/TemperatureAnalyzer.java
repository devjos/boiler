package de.schleger.boiler.analyze;

import de.schleger.boiler.information.InformationUpdater;
import de.schleger.boiler.temperature.Temperature;

public interface TemperatureAnalyzer extends InformationUpdater
{
	//TODO build average over the last x minutes/hours
	public Temperature getAverageTemperature();
}
