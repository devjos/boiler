package de.schleger.boiler.time;

import java.time.LocalDateTime;

public class TimeProviderImpl implements TimeProvider 
{
	private LocalDateTimeProvider dateTimeProvider = LocalDateTime::now;
	
	@Override
	public LocalDateTime getTime() 
	{
		return dateTimeProvider.now();
	}
	
	/**
	 * Zwischen 22Uhr und 6Uhr wird als Nacht ausgegeben
	 */
	@Override
	public boolean isNight() 
	{
		int hour = getTime().getHour();
		return 21 < hour || 6 > hour;
	}

	@Override
	public LocalDateTime getNextNachtheizungEndTime() 
	{
		
		LocalDateTime time = getTime();
		int dayOfMonth = time.getDayOfMonth();
		
		// Wenn größer 6 dann ist es der nächste Tag
		if(time.getHour() >= 6)
		{
			dayOfMonth++;
		}
		
		LocalDateTime dateTime = LocalDateTime.of(time.getYear(), time.getMonth(), dayOfMonth, 6, 0);
		return dateTime;
	}

	@Override
	public LocalDateTime addMinutesToTime(int minutes) 
	{		
		return getTime().plusMinutes(minutes);
	}
	
	public void setDateTimeProvider(LocalDateTimeProvider prov)
	{
		this.dateTimeProvider = prov;
	}	
}
