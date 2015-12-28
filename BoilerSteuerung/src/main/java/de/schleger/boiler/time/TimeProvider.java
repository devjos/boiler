package de.schleger.boiler.time;

import java.time.LocalDateTime;

import de.schleger.boiler.information.InformationProvider;

public interface TimeProvider extends InformationProvider
{
	public boolean isNight();
	
	public LocalDateTime getTime();
	
	public LocalDateTime getNextNachtheizungEndTime();

	public LocalDateTime addMinutesToTime(int minutes);
}

