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

import de.schleger.boiler.model.BoilerModel;

public class ConfigProviderOutFileTest 
{
	private static final String BOILER_CONF_OUT  = "boiler.config.out";
	private static final File FILE_OUT = new File(BOILER_CONF_OUT);
	private ConfigProviderOutFileImpl configProviderOut;
	private BoilerModel boilerModel;

	@Before
	public void setUp()
	{
		boilerModel = new BoilerModel();
		configProviderOut = new ConfigProviderOutFileImpl(FILE_OUT);
		boilerModel.addPropertyChangeListener(configProviderOut);
		
	}
		
	@Test
	public void writeHeatToFileAndCallsBoilerController() throws IOException
	{		
		boilerModel.setHeatPower(HeatPower.HEAT_POWER_3);		
		
		FileReader fileReader = new FileReader(FILE_OUT);		
		BufferedReader reader = new BufferedReader(fileReader);
				
		Properties prop = new Properties();
		prop.load(reader);
		
		assertThat(prop.getProperty(ConfigKeyOut.HEAT_LEVEL.toString()), equalTo(HeatPower.HEAT_POWER_3.toString()));
		assertThat(boilerModel.getHeatPower().toString(), equalTo(HeatPower.HEAT_POWER_3.toString()));
		
		reader.close();
	}
	
	@Test
	public void writeNotToHeatToFileAndCallsBoilerController() throws IOException
	{		
		boilerModel.setHeatPower(HeatPower.HEAT_POWER_0);
		
		FileReader fileReader = new FileReader(FILE_OUT);		
		
		Properties prop = new Properties();
		prop.load(fileReader);
		
		assertThat(prop.getProperty(ConfigKeyOut.HEAT_LEVEL.toString()), equalTo(HeatPower.HEAT_POWER_0.toString()));
		assertThat(boilerModel.getHeatPower().toString(), equalTo(HeatPower.HEAT_POWER_0.toString()));
		
		fileReader.close();
	}
	
	@Test
	public void canRememberHeatState() throws IOException
	{		
		boilerModel.setHeatPower(HeatPower.HEAT_POWER_2);
		assertThat(boilerModel.getHeatPower(), equalTo(HeatPower.HEAT_POWER_2));
	}
	
	
	@Test
	public void writeFillLevelToFile() throws IOException
	{		
		boilerModel.setFillLevel(38);
		
		FileReader fileReader = new FileReader(FILE_OUT);		
		BufferedReader reader = new BufferedReader(fileReader);
		
		Properties prop = new Properties();
		prop.load(reader);
		
		assertThat(prop.getProperty(ConfigKeyOut.FILL_LEVEL.toString()), equalTo("38.0"));
		
		reader.close();
	}
	
	@Test
	public void canRememberFillLevel() throws IOException
	{		
		boilerModel.setFillLevel(88);
		assertThat(boilerModel.getFillLevel(), equalTo(88F));
	}	
}
