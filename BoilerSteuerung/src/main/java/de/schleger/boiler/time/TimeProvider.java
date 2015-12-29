package de.schleger.boiler.time;

import java.time.LocalDateTime;

import de.schleger.boiler.information.InformationUpdater;

public interface TimeProvider extends InformationUpdater
{
	public boolean isNight();
	
	public LocalDateTime getTime();
	
	public LocalDateTime getNextNachtheizungEndTime();

	public LocalDateTime getNextLegionellenEndTime();
	
	public LocalDateTime addMinutesToTime(int minutes);
}

