package de.schleger.boiler.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Properties;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.schleger.boiler.temperature.Temperature;
import de.schleger.boiler.temperature.TemperatureImpl;

public class ConfigProviderFileImpl implements ConfigProvider 
{
	private static final Logger LOG = LogManager.getLogger(ConfigProviderFileImpl.class);
	
	private File file;
	private Properties prop;

	public ConfigProviderFileImpl(File file) 
	{
		this.file = file;
	}
	
	@Override
	public Temperature getTargetTemperature() 
	{
		readProperties();
		
		return new TemperatureImpl(Float.valueOf(prop.getProperty(ConfigKeys.TARGET_TEMPERATURE.toString())));
	}

	private void readProperties() 
	{
		prop = new Properties();		
		FileReader fileReader;
		
		try 
		{
			fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);
			prop.load(reader);			
		} 
		catch (Exception e) 
		{
			LOG.log(Level.ERROR, "Es konnte die Config nicht gelesen werden", e);
		}		
	}
}
