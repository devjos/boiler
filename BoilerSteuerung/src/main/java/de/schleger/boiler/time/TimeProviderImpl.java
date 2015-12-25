package de.schleger.boiler.time;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class TimeProviderImpl implements TimeProvider 
{
	private Calendar cal = GregorianCalendar.getInstance(Locale.GERMANY);
	
	@Override
	public Date getTime() 
	{
		return cal.getTime();
	}
	
	/**
	 * Zwischen 22Uhr und 6Uhr wird als Nacht ausgegeben
	 */
	@Override
	public boolean isNight() 
	{
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		
		if(21 < hour || 6 > hour)
		{
			return true;
		}
		
		return false;
	}

	@Override
	public Date getNextNachtheizungEndTime() 
	{
		Date time = getTime();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		
		// Wenn größer 6 dann ist es der nächste Tag
		if(cal.get(Calendar.HOUR_OF_DAY) >= 6)
		{
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		cal.set(Calendar.HOUR_OF_DAY, 6);
		cal.set(Calendar.MINUTE, 0);		
		cal.set(Calendar.SECOND, 0);		
		cal.set(Calendar.MILLISECOND, 0);		
		
		return cal.getTime();
	}

	@Override
	public Date addMinutesToTime(int minutes) 
	{		
		Calendar cal = Calendar.getInstance();
		cal.setTime(getTime());
		cal.add(Calendar.MINUTE, minutes);
		
		return cal.getTime();
	}
	
	public void setCalendar(Calendar cal)
	{
		this.cal = cal;		
	}	
}
