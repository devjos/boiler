package de.schleger.boiler.heat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.schleger.boiler.config.ConfigKeys;
import de.schleger.boiler.config.HeatPower;

public class HeatProviderFileImpl implements HeatProvider 
{
	private static final Logger LOG = LogManager.getLogger(HeatProviderFileImpl.class);
	
	private File file;
	private HeatPower heatPower;

	public HeatProviderFileImpl(File file) 
	{
		this.file = file;
	}
	
	@Override
	public void heat(HeatPower heatPower)
	{
		if(heatPower.equals(this.heatPower))
		{
			return;
		}
		
		this.heatPower = heatPower;
		
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
			try 
			{
				fileOutputStream.close();
			} 
			catch (IOException e) 
			{
				LOG.log(Level.ERROR, "Es konnte die Datei nicht geschrieben werden", e);
			}			
		}
	}

	@Override
	public HeatPower isHeating() 
	{
		return heatPower;
	}
}
