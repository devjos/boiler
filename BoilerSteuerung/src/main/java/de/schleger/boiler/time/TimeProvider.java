package de.schleger.boiler.time;

import java.util.Date;

public interface TimeProvider 
{
	public boolean isNight();
	
	public Date getTime();
	
	public Date getNextNachtheizungEndTime();

	public Date addMinutesToTime(int minutes);
}

