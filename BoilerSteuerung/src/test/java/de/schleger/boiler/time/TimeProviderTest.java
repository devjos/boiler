package de.schleger.boiler.time;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

public class TimeProviderTest 
{

	private TimeProviderImpl timeProvider;
	private Calendar cal;

	@Before
	public void setUp()
	{
		cal = GregorianCalendar.getInstance();
		timeProvider = new TimeProviderImpl();
		timeProvider.setCalendar(cal);
	}	
	
	@Test
	public void isNight()
	{	
		cal.set(2010, 8, 5, 22, 0);		
		assertThat(timeProvider.isNight(), equalTo(true));
		
		cal.set(2010, 8, 5, 5, 59);		
		assertThat(timeProvider.isNight(), equalTo(true));
	}
	
	@Test
	public void isDay()
	{	
		cal.set(2010, 8, 5, 21, 59);		
		assertThat(timeProvider.isNight(), equalTo(false));
		
		cal.set(2010, 8, 5, 6, 0);		
		assertThat(timeProvider.isNight(), equalTo(false));
	}
	
	@Test
	public void getBackTime()
	{	
		cal.set(2010, 8, 5, 21, 59, 59);		
		assertThat(timeProvider.getTime().toString(), equalTo("Sun Sep 05 21:59:59 CEST 2010"));
	}
	
	@Test
	public void canAddMinutesToTime()
	{	
		cal.set(2010, 8, 5, 21, 59, 59);		
		assertThat(timeProvider.addMinutesToTime(131).toString(), equalTo("Mon Sep 06 00:10:59 CEST 2010"));
		
		cal.set(2015, 8, 10, 6, 21, 40);		
		assertThat(timeProvider.addMinutesToTime(131).toString(), equalTo("Thu Sep 10 08:32:40 CEST 2015"));
	}
	
	@Test
	public void canCalcNextNachtzeizungEnd()
	{	
		cal.set(2010, 8, 5, 21, 59, 59);		
		assertThat(timeProvider.getNextNachtheizungEndTime().toString(), equalTo("Mon Sep 06 06:00:00 CEST 2010"));
		
		cal.set(2015, 8, 10, 6, 21, 40);
		assertThat(timeProvider.getNextNachtheizungEndTime().toString(), equalTo("Fri Sep 11 06:00:00 CEST 2015"));
	}
}
