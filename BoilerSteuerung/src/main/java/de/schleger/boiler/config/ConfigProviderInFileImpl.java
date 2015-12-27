package de.schleger.boiler.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.schleger.boiler.temperature.Temperature;
import de.schleger.boiler.temperature.TemperatureImpl;

public class ConfigProviderInFileImpl implements ConfigProviderIn 
{
	private static final Logger LOG = LogManager.getLogger(ConfigProviderInFileImpl.class);

	private static final float DEFAULT_VALUE_TARGET = 39;
	private static final float DEFAULT_VALUE_LEGIONELLEN = 65;
	
	private File file;
	private Properties prop;

	public ConfigProviderInFileImpl(File file) 
	{
		this.file = file;
	}
	
	@Override
	public Temperature getTargetTemperature() 
	{
		float temperature = DEFAULT_VALUE_TARGET;
		try{
			readProperties();
			temperature = Float.parseFloat(prop.getProperty(ConfigKeys.TARGET_TEMPERATURE.toString()));
		}
		catch ( Exception e){
			LOG.error("Unable to get target temperature from config file, use default instead: " + DEFAULT_VALUE_TARGET, e);
		}
		
		return new TemperatureImpl(temperature);
	}

	private void readProperties() throws IOException 
	{
		prop = new Properties();		
		FileReader fileReader = null;
		
		try 
		{
			fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);
			prop.load(reader);			
		}
		finally
		{
			IOUtils.closeQuietly(fileReader);
		}
	}

	@Override
	public Temperature getLegionellenTemperature() {
		float temperature = DEFAULT_VALUE_LEGIONELLEN;
		try{
			readProperties();
			temperature = Float.parseFloat(prop.getProperty(ConfigKeys.LEGIONELLEN_TEMPERATURE.toString()));
		}
		catch ( Exception e){
			LOG.error("Unable to get legionellen temperature from config file, use default instead: " + DEFAULT_VALUE_LEGIONELLEN, e);
		}
		
		return new TemperatureImpl(temperature);
	}
}
