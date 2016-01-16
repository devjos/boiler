package de.schleger.boiler.log;

public class LogDescriptor 
{
	private String path;
	private int lifeTimeInDays;

	public LogDescriptor(String path, int lifeTimeInDays) 
	{
		this.path = path;
		this.lifeTimeInDays = lifeTimeInDays;
	}
	
	public String getPath()
	{
		return path;
	}
	
	public int getLifeTimeInDays()
	{
		return lifeTimeInDays;
	}
}
