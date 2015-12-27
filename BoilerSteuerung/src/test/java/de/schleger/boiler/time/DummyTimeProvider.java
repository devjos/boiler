package de.schleger.boiler.time;

import java.time.LocalDateTime;

public class DummyTimeProvider implements TimeProvider
{
	private boolean isNight;
	private LocalDateTime nextNachtHezungEndTime;
	private LocalDateTime time;
	private LocalDateTime dateAddMinutesToTime;



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
	public LocalDateTime getTime() 
	{
		return time;
	}

	@Override
	public LocalDateTime getNextNachtheizungEndTime() 
	{
		return nextNachtHezungEndTime;
	}

	@Override
	public LocalDateTime addMinutesToTime(int minutes) 
	{
		return dateAddMinutesToTime;
	}

	public void setDateAddMinutesToTime(LocalDateTime dateAddMinutesToTime) {
		this.dateAddMinutesToTime = dateAddMinutesToTime;
	}
	
	public void setNextNachtHezungEndTime(LocalDateTime nextNachtHezungEndTime) 
	{
		this.nextNachtHezungEndTime = nextNachtHezungEndTime;
	}
}
