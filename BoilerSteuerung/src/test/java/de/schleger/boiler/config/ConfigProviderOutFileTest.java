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

import de.schleger.boiler.boilercontroller.DummyBoilerController;

public class ConfigProviderOutFileTest 
{
	private static final String BOILER_CONF_OUT  = "boiler.config.out";
	private static final File FILE_OUT = new File(BOILER_CONF_OUT);
	private ConfigProviderOutFileImpl heatFileImpl;
	private DummyBoilerController dummyBoilerController;

	@Before
	public void setUp()
	{
		dummyBoilerController = new DummyBoilerController();
		heatFileImpl = new ConfigProviderOutFileImpl(FILE_OUT, dummyBoilerController);
	}
		
	@Test
	public void writeHeatToFileAndCallsBoilerController() throws IOException
	{		
		heatFileImpl.setHeatPower(HeatPower.HEAT_POWER_3);		
		
		FileReader fileReader = new FileReader(FILE_OUT);		
		BufferedReader reader = new BufferedReader(fileReader);
				
		Properties prop = new Properties();
		prop.load(reader);
		
		assertThat(prop.getProperty(ConfigKeys.HEAT_LEVEL.toString()), equalTo(HeatPower.HEAT_POWER_3.toString()));
		assertThat(dummyBoilerController.getHeatPower().toString(), equalTo(HeatPower.HEAT_POWER_3.toString()));
		
		reader.close();
	}
	
	@Test
	public void writeNotToHeatToFileAndCallsBoilerController() throws IOException
	{		
		heatFileImpl.setHeatPower(HeatPower.HEAT_POWER_0);
		
		FileReader fileReader = new FileReader(FILE_OUT);		
		BufferedReader reader = new BufferedReader(fileReader);
		
		Properties prop = new Properties();
		prop.load(reader);
		
		assertThat(prop.getProperty(ConfigKeys.HEAT_LEVEL.toString()), equalTo(HeatPower.HEAT_POWER_0.toString()));
		assertThat(dummyBoilerController.getHeatPower().toString(), equalTo(HeatPower.HEAT_POWER_0.toString()));
		
		reader.close();
	}
	
	@Test
	public void canRememberHeatState() throws IOException
	{		
		heatFileImpl.setHeatPower(HeatPower.HEAT_POWER_2);
		assertThat(heatFileImpl.isHeating(), equalTo(HeatPower.HEAT_POWER_2));
	}
	
	
}
