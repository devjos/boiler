package de.schleger.boiler.analyze;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import de.schleger.boiler.temperature.Temperature;
import de.schleger.boiler.temperature.TemperatureImpl;

public class TemperatureAnalyzerFileImpl implements TemperatureAnalyzer
{
	private File file;


	public TemperatureAnalyzerFileImpl(File file) 
	{
		this.file = file;
	}
	
	@Override
	public Temperature getTemperature()
	{
		List<String> lines = new ArrayList<>();
		
		try 
		{
			lines = Files.readAllLines(file.toPath(),Charset.forName("UTF-8"));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		String[] split = lines.get(lines.size()-1).split(" ");
		
		return new TemperatureImpl(Float.valueOf(split[1]));
	}

}
