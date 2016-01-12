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
	private static final String TEMP_ERROR_LOG = "bin/temp_dirtyfile.log";
	private static final File FILE = new File(TEMP_LOG);
	private static final File FILE_ERROR = new File(TEMP_ERROR_LOG);
	
	private TemperatureAnalyzerFileImpl temperaturProviderFileImpl;


	@Before
	public void setUp()
	{
		temperaturProviderFileImpl = new TemperatureAnalyzerFileImpl(FILE);
		temperaturProviderFileImpl.update();
	}	
	
	@Test
	public void readAverageTemperatureFromFile() throws IOException
	{	
		Temperature temperatureImpl = new TemperatureImpl(27.003334f);
		
		assertThat(temperaturProviderFileImpl.getAverageTemperature(), equalTo(temperatureImpl));
	}
	
	@Test
	public void canHandleDirtyTempLog()
	{
		temperaturProviderFileImpl = new TemperatureAnalyzerFileImpl(FILE_ERROR);
		temperaturProviderFileImpl.update();

		Temperature temperatureImpl = new TemperatureImpl(100f);
		assertThat(temperaturProviderFileImpl.getAverageTemperature(), equalTo(temperatureImpl));
	}
}
