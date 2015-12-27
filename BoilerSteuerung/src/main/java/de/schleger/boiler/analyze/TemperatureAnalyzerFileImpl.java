package de.schleger.boiler.analyze;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.apache.commons.math3.stat.descriptive.moment.Mean;

import de.schleger.boiler.temperature.Temperature;
import de.schleger.boiler.temperature.TemperatureImpl;

public class TemperatureAnalyzerFileImpl implements TemperatureAnalyzer
{
	private static final int LINES_TO_READ = 5;
	private final File file;
	private static final Mean mean = new Mean();


	public TemperatureAnalyzerFileImpl(File file) 
	{
		this.file = file;
	}
	
	@Override
	public Temperature getTemperature()
	{
		
		List<String> lines = new ArrayList<>();
		
		ReversedLinesFileReader reader = null;
		try 
		{
			reader = new ReversedLinesFileReader(file);
			for ( int lineCounter = 0; lineCounter < LINES_TO_READ; lineCounter++ ){
				String line = reader.readLine();
				if ( line == null ){
					break;
				}
				lines.add(line);
			}
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		finally{
			IOUtils.closeQuietly(reader);
		}
		
		double[] temperatures = new double[lines.size()];
		
		for ( int i=0; i<lines.size(); i++){
			temperatures[i] = Float.parseFloat(lines.get(i).split(" ")[1]);
		}
		
		return new TemperatureImpl((float) mean.evaluate(temperatures));
	}

}
