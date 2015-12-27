package de.schleger.boiler.analyze;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import de.schleger.boiler.temperature.Temperature;
import de.schleger.boiler.temperature.TemperatureImpl;

public class TemperatureAnalyzerFileTest 
{
	private static final String TEMP_LOG = "temp.log";
	private static final File FILE = new File(TEMP_LOG);
	
	private TemperatureAnalyzerFileImpl temperaturProviderFileImpl;


	@Before
	public void setUp()
	{
		temperaturProviderFileImpl = new TemperatureAnalyzerFileImpl(FILE);
	}	
	
	@Test
	public void readAverageTemperatureFromFile() throws IOException
	{	
		Temperature temperatureImpl = new TemperatureImpl(27.003334f);
		
		assertThat(temperaturProviderFileImpl.getAverageTemperature(), equalTo(temperatureImpl));
	}
	
	@Test
	public void readLastTemperatureFromFile(){
		Temperature temperatureImpl = new TemperatureImpl(28.67f);
		assertThat(temperaturProviderFileImpl.getLastTemperature(), equalTo(temperatureImpl));
	}
}
