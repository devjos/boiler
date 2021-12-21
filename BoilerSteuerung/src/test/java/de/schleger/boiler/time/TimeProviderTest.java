package de.schleger.boiler.time;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

public class TimeProviderTest 
{

	private TimeProviderImpl timeProvider;

	@Before
	public void setUp()
	{
		timeProvider = new TimeProviderImpl();
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
	public void canCalcNextNachtHeizungEnd()
	{	
		timeProvider.setDateTimeProvider( () -> LocalDateTime.of(2010, 12, 31, 21, 59, 59) );
		assertThat(timeProvider.getNextNachtheizungEndTime().toString(), equalTo("2011-01-01T06:00"));
		
		timeProvider.setDateTimeProvider( () -> LocalDateTime.of(2010, 8, 10, 6, 21, 40) );
		assertThat(timeProvider.getNextNachtheizungEndTime().toString(), equalTo("2010-08-11T06:00"));
	}
	
	@Test
	public void canCalcNextLeggionellenHeizungEnd()
	{	
		timeProvider.setDateTimeProvider( () -> LocalDateTime.of(2010, 8, 5, 21, 59, 59) );
		assertThat(timeProvider.getNextLegionellenEndTime().toString(), equalTo("2010-09-01T06:00"));
		
		timeProvider.setDateTimeProvider( () -> LocalDateTime.of(2010, 2, 1, 6, 21, 40) );
		assertThat(timeProvider.getNextLegionellenEndTime().toString(), equalTo("2010-03-01T06:00"));
	}
}
