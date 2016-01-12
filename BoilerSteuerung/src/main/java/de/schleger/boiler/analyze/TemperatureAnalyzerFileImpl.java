package de.schleger.boiler.analyze;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.schleger.boiler.temperature.Temperature;
import de.schleger.boiler.temperature.TemperatureImpl;

public class TemperatureAnalyzerFileImpl implements TemperatureAnalyzer
{
	private static final Logger LOG = LogManager.getLogger(TemperatureAnalyzerFileImpl.class);
	
	private static final int LINES_TO_READ_DEFAULT = 5;	
	private final File file;

	// Falls beim ersten Versuch gar nichts gelesen werden kann ist Sicherheitshalber 100 Grad hinterlegt
	private Float lastValidAverageTemperature = 100f;

	public TemperatureAnalyzerFileImpl(File file) 
	{
		this.file = file;
	}
	
	@Override
	public Temperature getAverageTemperature()
	{		
		return new TemperatureImpl(lastValidAverageTemperature);
	}
	
	private void readTemperatures(int linesToRead)
	{
		List<Float> temperaturesList = new ArrayList<>();

		ReversedLinesFileReader reader = null;
		try 
		{
			reader = new ReversedLinesFileReader(file);
			for ( int lineCounter = 0; lineCounter < linesToRead; lineCounter++ ){
				String line = reader.readLine();
				if ( line == null ){
					break;
				}
				Float valueOf = Float.valueOf(line.split(" ")[1]);
				temperaturesList.add(valueOf);				
			}
			
			Float sum = temperaturesList.stream().reduce(0.0f, (a, b) -> Float.sum(a, b));
			lastValidAverageTemperature = sum / temperaturesList.size();
			
		} 
		catch (Exception e) 
		{
			LOG.log(Level.ERROR, "Fehler beim lesen/ermitteln der Temperaturen aus dem File", e);
		}
		finally
		{
			IOUtils.closeQuietly(reader);
		}
	}

	@Override
	public void update() 
	{
		readTemperatures(LINES_TO_READ_DEFAULT);
	}
}
