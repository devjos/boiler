package de.schleger.boiler.config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import de.schleger.boiler.temperature.Temperature;
import de.schleger.boiler.temperature.TemperatureImpl;

public class ConfigProviderInFileTest 
{
	private static final String BOILER_CONF_IN = "boiler.config.in";
	private static final File FILE_IN = new File(BOILER_CONF_IN);
	
	private ConfigProviderInFileImpl configProviderFileImpl;


	@Before
	public void setUp()
	{
		configProviderFileImpl = new ConfigProviderInFileImpl(FILE_IN);
		configProviderFileImpl.update();
	}	
	
	@Test
	public void readTargetTemperatureFromFile() throws IOException
	{	
		Temperature temperatureImpl = new TemperatureImpl(38.00f);
		
		assertThat(configProviderFileImpl.getTargetTemperature(), equalTo(temperatureImpl));
	}
	
	@Test
	public void readLegionellenTemperatureFromFile() throws IOException
	{	
		Temperature temperatureImpl = new TemperatureImpl(55.00f);
		
		assertThat(configProviderFileImpl.getLegionellenTemperature(), equalTo(temperatureImpl));
	}
	
	@Test
	public void readEmptyTemperatureFromFile() throws IOException
	{	
		Temperature temperatureImpl = new TemperatureImpl(10.00f);
		
		assertThat(configProviderFileImpl.getEmptyTemperature(), equalTo(temperatureImpl));
	}
	
	@Test
	public void readPriceNightFromFile() throws IOException
	{	
		assertThat(configProviderFileImpl.getEuroPerKwhNight(), equalTo(0.18f));
	}
	
	@Test
	public void readPriceDayFromFile() throws IOException
	{	
		assertThat(configProviderFileImpl.getEuroPerKwhDay(), equalTo(0.21f));
	}
	
	@Test
	public void useDefaultValueIfFileIsNotPresent() throws IOException
	{	
		configProviderFileImpl = new ConfigProviderInFileImpl(new File("123xyz"));
		configProviderFileImpl.update();
		
		Temperature targetTemperature = new TemperatureImpl(40.00f);		
		assertThat(configProviderFileImpl.getTargetTemperature(), equalTo(targetTemperature));
		
		Temperature legionellenTemperature = new TemperatureImpl(50.00f);		
		assertThat(configProviderFileImpl.getLegionellenTemperature(), equalTo(legionellenTemperature));
		
		Temperature EmptyTemperature = new TemperatureImpl(15.00f);
		assertThat(configProviderFileImpl.getEmptyTemperature(), equalTo(EmptyTemperature));
		
		assertThat(configProviderFileImpl.getEuroPerKwhDay(), equalTo(0.185283f));
		assertThat(configProviderFileImpl.getEuroPerKwhNight(), equalTo(0.216342f));
	}
}
