package de.schleger.boiler.config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import de.schleger.boiler.temperature.Temperature;
import de.schleger.boiler.temperature.TemperatureImpl;

public class ConfigProviderFileTest 
{
	private static final String BOILER_CONF_IN = "boiler.config.in";
	private static final File FILE_IN = new File(BOILER_CONF_IN);
	
	private ConfigProviderInFileImpl configProviderFileImpl;


	@Before
	public void setUp()
	{
		configProviderFileImpl = new ConfigProviderInFileImpl(FILE_IN);
		configProviderFileImpl.updateInformation();
	}	
	
	@Test
	public void readTemperatureFromFile() throws IOException
	{	
		Temperature temperatureImpl = new TemperatureImpl(40.00f);
		
		assertThat(configProviderFileImpl.getTargetTemperature(), equalTo(temperatureImpl));
	}
	
	@Test
	public void useDefaultValueIfFileIsNotPresent() throws IOException
	{	
		configProviderFileImpl = new ConfigProviderInFileImpl(new File("123xyz"));
		configProviderFileImpl.updateInformation();
		Temperature temperatureImpl = new TemperatureImpl(39.00f);
		
		assertThat(configProviderFileImpl.getTargetTemperature(), equalTo(temperatureImpl));
	}
}
