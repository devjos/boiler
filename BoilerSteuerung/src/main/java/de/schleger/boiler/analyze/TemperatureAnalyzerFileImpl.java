package de.schleger.boiler.analyze;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReversedLinesFileReader;

import de.schleger.boiler.temperature.Temperature;
import de.schleger.boiler.temperature.TemperatureImpl;

public class TemperatureAnalyzerFileImpl implements TemperatureAnalyzer
{
	private static final int LINES_TO_READ_DEFAULT = 5;
	private final File file;

	public TemperatureAnalyzerFileImpl(File file) 
	{
		this.file = file;
	}
	
	@Override
	public Temperature getAverageTemperature()
	{
		List<Float> temperaturesList = readTemperatures(LINES_TO_READ_DEFAULT);
		
		//build average
		Float sum = temperaturesList.stream().reduce(0.0f, Float::sum);
		return new TemperatureImpl(sum / temperaturesList.size());
	}

	@Override
	public Temperature getLastTemperature() {
		return new TemperatureImpl( readLastTemperature() );
	}
	
	private float readLastTemperature(){
		return readTemperatures(1).get(0).floatValue();
	}
	
	private List<Float> readTemperatures(int linesToRead){
		List<Float> lines = new ArrayList<>();
		ReversedLinesFileReader reader = null;
		try 
		{
			reader = new ReversedLinesFileReader(file);
			for ( int lineCounter = 0; lineCounter < linesToRead; lineCounter++ ){
				String line = reader.readLine();
				if ( line == null ){
					break;
				}
				lines.add(Float.valueOf(line.split(" ")[1]));
			}
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			IOUtils.closeQuietly(reader);
		}
		return lines;
	}

}
