package de.schleger.boiler.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.schleger.boiler.boiler.BoilerController;

public class ConfigProviderOutFileImpl implements ConfigProviderOut 
{
	private static final Logger LOG = LogManager.getLogger(ConfigProviderOutFileImpl.class);
	
	private BoilerController boilerController;
	
	private Properties prop;
	private File file;
	
	private HeatPower heatPower;
	private int fillLevel;

	public ConfigProviderOutFileImpl(File file, BoilerController boilerController) 
	{
		this.file = file;
		this.boilerController = boilerController;
		
		prop = new Properties();
	}
	
	@Override
	public void setHeatPower(HeatPower heatPower)
	{		
		if(heatPower.equals(this.heatPower))
		{
			return;
		}
		boilerController.setHeatPower(heatPower);
		
		this.heatPower = heatPower;
		prop.setProperty(ConfigKeyOut.HEAT_LEVEL.toString(), heatPower.toString());
		
		writePropertiesToFile();
	}
	
	@Override
	public HeatPower isHeating() 
	{
		return heatPower;
	}
	
	@Override
	public void setFillLevel(int fillLevel) 
	{
		if(this.fillLevel == fillLevel)
		{
			return;
		}
		this.fillLevel = fillLevel;
		prop.setProperty(ConfigKeyOut.FILL_LEVEL.toString(), Integer.toString(fillLevel));
		
		writePropertiesToFile();		
	}

	@Override
	public int getFillLevel() 
	{
		return fillLevel;
	}

	private void writePropertiesToFile() 
	{
		LOG.log(Level.INFO, prop.entrySet());
		
		FileOutputStream fileOutputStream = null;
		
		try 
		{
			fileOutputStream = new FileOutputStream(file);
			prop.store(fileOutputStream, null);
		} 
		catch (IOException e) 
		{
			LOG.log(Level.ERROR, "Es konnte die Datei nicht geschrieben werden", e);
		}
		finally
		{
			IOUtils.closeQuietly(fileOutputStream);			
		}
	}
}
