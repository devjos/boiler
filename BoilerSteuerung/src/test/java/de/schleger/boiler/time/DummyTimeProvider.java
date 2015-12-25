package de.schleger.boiler.time;

import java.util.Date;

public class DummyTimeProvider implements TimeProvider
{
	private boolean isNight;
	private Date nextNachtHezungEndTime;
	private Date time;
	private Date dateAddMinutesToTime;



	@Override
	public boolean isNight() 
	{
		return isNight;
	}
	
	public void setNight(boolean isNight)
	{
		this.isNight = isNight;		
	}

	@Override
	public Date getTime() 
	{
		return time;
	}

	@Override
	public Date getNextNachtheizungEndTime() 
	{
		return nextNachtHezungEndTime;
	}

	@Override
	public Date addMinutesToTime(int minutes) 
	{
		return dateAddMinutesToTime;
	}

	public void setDateAddMinutesToTime(Date dateAddMinutesToTime) {
		this.dateAddMinutesToTime = dateAddMinutesToTime;
	}
	
	public void setNextNachtHezungEndTime(Date nextNachtHezungEndTime) 
	{
		this.nextNachtHezungEndTime = nextNachtHezungEndTime;
	}
}
