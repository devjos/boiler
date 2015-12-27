package de.schleger.boiler.filter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import de.schleger.boiler.analyze.DummyTemperatureAnalyzer;
import de.schleger.boiler.config.DummyConfigProvider;
import de.schleger.boiler.config.DummyConfigProviderOut;
import de.schleger.boiler.config.HeatPower;
import de.schleger.boiler.heat.DummyHeatTimeCalculator;
import de.schleger.boiler.temperature.TemperatureImpl;
import de.schleger.boiler.time.DummyTimeProvider;

public class TemperatureActionFilterNachtHeizungTest 
{
	private DummyTimeProvider dummyTimeProvider;
	private DummyConfigProvider dummyConfigProvider;
	private DummyTemperatureAnalyzer dummyTemperatureAnalyzer;
	private DummyConfigProviderOut dummyConfigProviderOut;
	private DummyHeatTimeCalculator dummyHeatTimeCalculator;
	
	private TemperatureActionFilterNachtHeizung mindesttemperaturFilter;
	
	@Before
	public void setUp()
	{
		dummyTimeProvider = new DummyTimeProvider();
		dummyConfigProvider = new DummyConfigProvider();
		dummyTemperatureAnalyzer = new DummyTemperatureAnalyzer();
		dummyConfigProviderOut = new DummyConfigProviderOut();
		dummyHeatTimeCalculator = new DummyHeatTimeCalculator();
		mindesttemperaturFilter = new TemperatureActionFilterNachtHeizung(
				dummyTimeProvider, dummyConfigProvider, dummyTemperatureAnalyzer, dummyConfigProviderOut, dummyHeatTimeCalculator);
	}

	
	@Test
	public void heitzNichtAmTag()
	{
		dummyTimeProvider.setNight(false);
		
		assertThat(mindesttemperaturFilter.filter(), equalTo(false));
		assertThat(dummyConfigProviderOut.isHeating(), equalTo(HeatPower.HEAT_POWER_0));		
	}
	
	@Test
	public void noHeatIfNoHeatingTime()
	{
		dummyTimeProvider.setNight(true);
		dummyHeatTimeCalculator.setTimetoHeat(0);
		dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(60f));
		dummyConfigProvider.setTargetTemperature(new TemperatureImpl(50f));
		
		assertThat(mindesttemperaturFilter.filter(), equalTo(false));		
		assertThat(dummyHeatTimeCalculator.getStartTemp().getTemperature(), equalTo(60f));		
		assertThat(dummyHeatTimeCalculator.getEndTemp().getTemperature(), equalTo(50f));		
		assertThat(dummyHeatTimeCalculator.getHeatPower(), equalTo(HeatPower.HEAT_POWER_3));		
		assertThat(dummyConfigProviderOut.isHeating(), equalTo(HeatPower.HEAT_POWER_0));		
	}
	
	@Test
	public void waitsForHeatingEndTime()
	{		
		LocalDateTime timecalculated = LocalDateTime.of(2010, 8, 5, 21, 0);
		LocalDateTime endNachtheizung = LocalDateTime.of(2010, 8, 5, 22, 0);
		
		dummyTimeProvider.setNight(true);
		dummyHeatTimeCalculator.setTimetoHeat(60);
		dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(60f));
		dummyConfigProvider.setTargetTemperature(new TemperatureImpl(50f));
		dummyTimeProvider.setDateAddMinutesToTime(timecalculated);
		dummyTimeProvider.setNextNachtHezungEndTime(endNachtheizung);
		
		assertThat(mindesttemperaturFilter.filter(), equalTo(false));		
		assertThat(dummyConfigProviderOut.isHeating(), equalTo(HeatPower.HEAT_POWER_0));		
	}
	
	@Test
	public void heatingEndTimeIsOver()
	{
		LocalDateTime timecalculated = LocalDateTime.of(2010, 8, 5, 23, 0);
		LocalDateTime endNachtheizung = LocalDateTime.of(2010, 8, 5, 22, 0);
		
		dummyTimeProvider.setNight(true);
		dummyHeatTimeCalculator.setTimetoHeat(60);
		dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(60f));
		dummyConfigProvider.setTargetTemperature(new TemperatureImpl(50f));
		dummyTimeProvider.setDateAddMinutesToTime(timecalculated);
		dummyTimeProvider.setNextNachtHezungEndTime(endNachtheizung);
		
		assertThat(mindesttemperaturFilter.filter(), equalTo(true));		
		assertThat(dummyConfigProviderOut.isHeating(), equalTo(HeatPower.HEAT_POWER_3));		
	}
	
}
