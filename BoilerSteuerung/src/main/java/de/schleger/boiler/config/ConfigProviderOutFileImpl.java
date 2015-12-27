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
	
	private File file;
	private HeatPower heatPower;

	private BoilerController boilerController;

	public ConfigProviderOutFileImpl(File file, BoilerController boilerController) 
	{
		this.file = file;
		this.boilerController = boilerController;
	}
	
	@Override
	public void setHeatPower(HeatPower heatPower)
	{		
		if(heatPower.equals(this.heatPower))
		{
			return;
		}
		
		this.heatPower = heatPower;
		boilerController.setHeatPower(heatPower);		
		
		Properties prop = new Properties();
		prop.setProperty(ConfigKeys.HEAT_LEVEL.toString(), heatPower.toString());
		
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

	@Override
	public HeatPower isHeating() 
	{
		return heatPower;
	}
}
