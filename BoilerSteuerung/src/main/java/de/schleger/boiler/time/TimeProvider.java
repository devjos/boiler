package de.schleger.boiler.time;

import java.time.LocalDateTime;

public interface TimeProvider 
{
	public boolean isNight();
	
	public LocalDateTime getTime();
	
	public LocalDateTime getNextNachtheizungEndTime();

	public LocalDateTime addMinutesToTime(int minutes);
}

