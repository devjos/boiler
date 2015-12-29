package de.schleger.boiler.filter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.schleger.boiler.analyze.TemperatureAnalyzer;
import de.schleger.boiler.temperature.Temperature;
import de.schleger.boiler.temperature.TemperatureImpl;
import de.schleger.boiler.time.TimeProvider;

public class TemperatureActionFilterMindesttemperaturUnterTag implements TemperatureActionFilter {

	private static final Logger LOG = LogManager.getLogger(TemperatureActionFilterMindesttemperaturUnterTag.class);
	
	// TODO Aus Config verwenden
	private static final Temperature MIN_TEMPERATURE = new TemperatureImpl(20.00f);
	private TemperatureAnalyzer analyzer;
	private TimeProvider timeProvider;
	
	public TemperatureActionFilterMindesttemperaturUnterTag(TemperatureAnalyzer analyzer, TimeProvider timeProvider) 
	{
		this.analyzer = analyzer;
		this.timeProvider = timeProvider;
	}
	
	@Override
	public boolean filter() 
	{
		// TODO heizt noch nicht
		if(!timeProvider.isNight() && MIN_TEMPERATURE.compareTo(analyzer.getAverageTemperature()) > 0)
		{
			LOG.log(Level.INFO, "Mindesttemperatur unterschritten, erzeuge Target mit Temperatur: " + MIN_TEMPERATURE.getTemperature());
			
			return true;
		}
		
		return false;
	}

}
