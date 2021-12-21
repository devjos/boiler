package de.schleger.boiler.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.schleger.boiler.temperature.Temperature;
import de.schleger.boiler.temperature.TemperatureImpl;

public class ConfigProviderInFileImpl implements ConfigProviderIn 
{
	private static final Logger LOG = LogManager.getLogger(ConfigProviderInFileImpl.class);
	
	private final File file;
	
	private final HashMap<ConfigKeyIn, Float> config = new HashMap<>();

	public ConfigProviderInFileImpl(File file) 
	{
		this.file = file;
	}
	
	@Override
	public Temperature getTargetTemperature() 
	{
		return new TemperatureImpl(config.get(ConfigKeyIn.TARGET_TEMPERATURE));
	}
	
	@Override
	public Temperature getLegionellenTemperature() 
	{
		return new TemperatureImpl(config.get(ConfigKeyIn.LEGIONELLEN_TEMPERATURE));
	}
	
	@Override
	public Temperature getEmptyTemperature() 
	{
		return new TemperatureImpl(config.get(ConfigKeyIn.EMPTY_TEMPERATURE));
	}
	
	@Override
	public float getEuroPerKwhNight() {
		return config.get(ConfigKeyIn.EURO_PER_KWH_NIGHT);
	}

	@Override
	public float getEuroPerKwhDay() {
		return config.get(ConfigKeyIn.EURO_PER_KWH_DAY);
	}

	private Properties readProperties() throws IOException 
	{
		Properties prop = new Properties();		
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
		return prop;
	}


	@Override
	public void update() {
		insertDefaultValues();
		try{
			Properties prop = readProperties();
			for ( ConfigKeyIn key : ConfigKeyIn.values() ){
				try{
					config.put(key, Float.parseFloat(prop.getProperty(key.toString())));
				}catch( Exception e){
					LOG.info("Cannot convert config key " + key);
				}
			}
		} catch( Exception e){
			LOG.error("Unable to read config file", e);
		}
	}
	
	private void insertDefaultValues() {
		for ( ConfigKeyIn key : ConfigKeyIn.values() ){
			config.put(key, ConfigKeyIn.getDefault(key));
		}
	}

}
