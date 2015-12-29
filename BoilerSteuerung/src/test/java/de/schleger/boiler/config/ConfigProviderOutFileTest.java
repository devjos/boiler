package de.schleger.boiler.config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import de.schleger.boiler.boiler.DummyBoilerController;

public class ConfigProviderOutFileTest 
{
	private static final String BOILER_CONF_OUT  = "boiler.config.out";
	private static final File FILE_OUT = new File(BOILER_CONF_OUT);
	private ConfigProviderOutFileImpl configProviderOut;
	private DummyBoilerController dummyBoilerController;

	@Before
	public void setUp()
	{
		dummyBoilerController = new DummyBoilerController();
		configProviderOut = new ConfigProviderOutFileImpl(FILE_OUT, dummyBoilerController);
	}
		
	@Test
	public void writeHeatToFileAndCallsBoilerController() throws IOException
	{		
		configProviderOut.setHeatPower(HeatPower.HEAT_POWER_3);		
		
		FileReader fileReader = new FileReader(FILE_OUT);		
		BufferedReader reader = new BufferedReader(fileReader);
				
		Properties prop = new Properties();
		prop.load(reader);
		
		assertThat(prop.getProperty(ConfigKeyOut.HEAT_LEVEL.toString()), equalTo(HeatPower.HEAT_POWER_3.toString()));
		assertThat(dummyBoilerController.getHeatPower().toString(), equalTo(HeatPower.HEAT_POWER_3.toString()));
		
		reader.close();
	}
	
	@Test
	public void writeNotToHeatToFileAndCallsBoilerController() throws IOException
	{		
		configProviderOut.setHeatPower(HeatPower.HEAT_POWER_0);
		
		FileReader fileReader = new FileReader(FILE_OUT);		
		BufferedReader reader = new BufferedReader(fileReader);
		
		Properties prop = new Properties();
		prop.load(reader);
		
		assertThat(prop.getProperty(ConfigKeyOut.HEAT_LEVEL.toString()), equalTo(HeatPower.HEAT_POWER_0.toString()));
		assertThat(dummyBoilerController.getHeatPower().toString(), equalTo(HeatPower.HEAT_POWER_0.toString()));
		
		reader.close();
	}
	
	@Test
	public void canRememberHeatState() throws IOException
	{		
		configProviderOut.setHeatPower(HeatPower.HEAT_POWER_2);
		assertThat(configProviderOut.isHeating(), equalTo(HeatPower.HEAT_POWER_2));
	}
	
	
	@Test
	public void writeFillLevelToFile() throws IOException
	{		
		configProviderOut.setFillLevel(38);
		
		FileReader fileReader = new FileReader(FILE_OUT);		
		BufferedReader reader = new BufferedReader(fileReader);
		
		Properties prop = new Properties();
		prop.load(reader);
		
		assertThat(prop.getProperty(ConfigKeyOut.FILL_LEVEL.toString()), equalTo("38"));
		
		reader.close();
	}
	
	@Test
	public void canRememberFillLevel() throws IOException
	{		
		configProviderOut.setFillLevel(88);
		assertThat(configProviderOut.getFillLevel(), equalTo(88));
	}	
}
