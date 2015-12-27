package de.schleger.boiler.time;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

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
		
		if(21 < hour || 6 > hour)
		{
			return true;
		}
		
		return false;
	}

	@Override
	public LocalDateTime getNextNachtheizungEndTime() 
	{
		
		LocalDateTime time = getTime();
		int dayOfMonth = time.getDayOfMonth();
		
		// Wenn gr��er 6 dann ist es der n�chste Tag
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
		ZonedDateTime t = getTime().atZone(ZoneOffset.UTC);
		t = t.plusMinutes(minutes);
		return t.toLocalDateTime();
	}
	
	public void setDateTimeProvider(LocalDateTimeProvider prov)
	{
		this.dateTimeProvider = prov;
	}	
}
