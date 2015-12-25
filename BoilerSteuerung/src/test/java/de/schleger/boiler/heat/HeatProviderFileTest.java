package de.schleger.boiler.heat;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import de.schleger.boiler.config.ConfigKeys;
import de.schleger.boiler.config.HeatPower;
import de.schleger.boiler.heat.HeatProviderFileImpl;

public class HeatProviderFileTest 
{
	private static final String BOILER_CONF_OUT  = "boiler.config.out";
	private static final File FILE_OUT = new File(BOILER_CONF_OUT);
	private HeatProviderFileImpl heatFileImpl;

	@Before
	public void setUp()
	{
		heatFileImpl = new HeatProviderFileImpl(FILE_OUT);
	}
		
	@Test
	public void writeHeatToFile() throws IOException
	{		
		heatFileImpl.heat(HeatPower.HEAT_POWER_3);		
		
		FileReader fileReader = new FileReader(FILE_OUT);		
		BufferedReader reader = new BufferedReader(fileReader);
				
		Properties prop = new Properties();
		prop.load(reader);
		
		assertThat(prop.getProperty(ConfigKeys.HEAT_LEVEL.toString()), equalTo(HeatPower.HEAT_POWER_3.toString()));
		
		reader.close();
	}
	
	@Test
	public void writeNotToHeatToFile() throws IOException
	{		
		heatFileImpl.heat(HeatPower.HEAT_POWER_0);
		
		FileReader fileReader = new FileReader(FILE_OUT);		
		BufferedReader reader = new BufferedReader(fileReader);
		
		Properties prop = new Properties();
		prop.load(reader);
		
		assertThat(prop.getProperty(ConfigKeys.HEAT_LEVEL.toString()), equalTo(HeatPower.HEAT_POWER_0.toString()));
		
		reader.close();
	}
	
	@Test
	public void canRememberHeatState() throws IOException
	{		
		heatFileImpl.heat(HeatPower.HEAT_POWER_2);
		assertThat(heatFileImpl.isHeating(), equalTo(HeatPower.HEAT_POWER_2));
	}
	
	
}
