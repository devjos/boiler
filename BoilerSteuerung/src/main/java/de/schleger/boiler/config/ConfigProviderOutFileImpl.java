package de.schleger.boiler.config;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigProviderOutFileImpl implements PropertyChangeListener
{
	private static final Logger LOG = LogManager.getLogger(ConfigProviderOutFileImpl.class);
	
	private final Properties prop = new Properties();
	private final File file;


	public ConfigProviderOutFileImpl(File file) 
	{
		this.file = file;
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

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch (evt.getPropertyName()) {
		case "heatPower":
			HeatPower heatPower = (HeatPower)evt.getNewValue();
			prop.setProperty(ConfigKeyOut.HEAT_LEVEL.toString(), heatPower.toString());		
			writePropertiesToFile();
			break;
			
		case "fillLevel":
			float fillLevel = (float)evt.getNewValue();
			prop.setProperty(ConfigKeyOut.FILL_LEVEL.toString(), Float.toString(fillLevel));	
			writePropertiesToFile();		
			break;

		default:
			break;
		}
		
	}
	
}
