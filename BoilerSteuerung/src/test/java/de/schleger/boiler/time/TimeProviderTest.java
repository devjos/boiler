package de.schleger.boiler.time;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

public class TimeProviderTest 
{

	private TimeProviderImpl timeProvider;

	@Before
	public void setUp()
	{
		timeProvider = new TimeProviderImpl();
		timeProvider.setDateTimeProvider(LocalDateTime::now);
	}	
	
	@Test
	public void isNight()
	{	
		timeProvider.setDateTimeProvider( () -> LocalDateTime.of(2010, 8, 5, 22, 0) );
		assertThat(timeProvider.isNight(), equalTo(true));
		
		timeProvider.setDateTimeProvider( () -> LocalDateTime.of(2010, 8, 5, 5, 59) );		
		assertThat(timeProvider.isNight(), equalTo(true));
	}
	
	@Test
	public void isDay()
	{	
		timeProvider.setDateTimeProvider( () -> LocalDateTime.of(2010, 8, 5, 21, 59) );
		assertThat(timeProvider.isNight(), equalTo(false));
		
		timeProvider.setDateTimeProvider( () -> LocalDateTime.of(2010, 8, 5, 6, 0) );
		assertThat(timeProvider.isNight(), equalTo(false));
	}
	
	@Test
	public void getBackTime()
	{	
		timeProvider.setDateTimeProvider( () -> LocalDateTime.of(2010, 8, 5, 21, 59, 59) );
		assertThat(timeProvider.getTime().toString(), equalTo("2010-08-05T21:59:59"));
	}
	
	@Test
	public void canAddMinutesToTime()
	{	
		timeProvider.setDateTimeProvider( () -> LocalDateTime.of(2010, 8, 5, 21, 59, 59) );
		assertThat(timeProvider.addMinutesToTime(131).toString(), equalTo("2010-08-06T00:10:59"));
		
		timeProvider.setDateTimeProvider( () -> LocalDateTime.of(2010, 8, 10, 6, 21, 40) );
		assertThat(timeProvider.addMinutesToTime(131).toString(), equalTo("2010-08-10T08:32:40"));
	}
	
	@Test
	public void canCalcNextNachtzeizungEnd()
	{	
		timeProvider.setDateTimeProvider( () -> LocalDateTime.of(2010, 8, 5, 21, 59, 59) );
		assertThat(timeProvider.getNextNachtheizungEndTime().toString(), equalTo("2010-08-06T06:00"));
		
		timeProvider.setDateTimeProvider( () -> LocalDateTime.of(2010, 8, 10, 6, 21, 40) );
		assertThat(timeProvider.getNextNachtheizungEndTime().toString(), equalTo("2010-08-11T06:00"));
	}
}
