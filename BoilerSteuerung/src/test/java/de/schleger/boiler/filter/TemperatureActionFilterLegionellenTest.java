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
import de.schleger.boiler.time.TimeProviderImpl;

public class TemperatureActionFilterLegionellenTest 
{
	private TimeProviderImpl timeProvider;
	private DummyConfigProvider dummyConfigProvider;
	private DummyTemperatureAnalyzer dummyTemperatureAnalyzer;
	private DummyConfigProviderOut dummyConfigProviderOut;
	private DummyHeatTimeCalculator dummyHeatTimeCalculator;
	
	private TemperatureActionFilterLegionellen legionellenFilter;
	
	@Before
	public void setUp()
	{
		timeProvider = new TimeProviderImpl();
		dummyConfigProvider = new DummyConfigProvider();
		dummyTemperatureAnalyzer = new DummyTemperatureAnalyzer();
		dummyConfigProviderOut = new DummyConfigProviderOut();
		dummyHeatTimeCalculator = new DummyHeatTimeCalculator();
		legionellenFilter = new TemperatureActionFilterLegionellen(
				timeProvider, dummyConfigProvider, dummyTemperatureAnalyzer, dummyConfigProviderOut, dummyHeatTimeCalculator);
	}

	
	@Test
	public void heiztNichtAmTag()
	{
		timeProvider.setDateTimeProvider( () -> LocalDateTime.of(2015, 12, 11, 10, 9) );
		
		assertThat(legionellenFilter.filter(), equalTo(false));
		assertThat(dummyConfigProviderOut.isHeating(), equalTo(HeatPower.HEAT_POWER_0));		
	}
	
	@Test
	public void heiztInDerNachtVonSoAufMo()
	{
		timeProvider.setDateTimeProvider( () -> LocalDateTime.of(2015, 12, 28, 4, 00) );
		
		dummyHeatTimeCalculator.setTimetoHeat(121);
		dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(50f));
		dummyConfigProvider.setLegionellenTemperature(new TemperatureImpl(65f));
		
		assertThat(legionellenFilter.filter(), equalTo(true));		
		assertThat(dummyConfigProviderOut.isHeating(), equalTo(HeatPower.HEAT_POWER_3));
	}
	
	@Test
	public void heiztNichtAnAnderenWochentagen()
	{
		timeProvider.setDateTimeProvider( () -> LocalDateTime.of(2015, 12, 29, 4, 00) );
		
		dummyHeatTimeCalculator.setTimetoHeat(121);
		dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(50f));
		dummyConfigProvider.setLegionellenTemperature(new TemperatureImpl(65f));
		
		assertThat(legionellenFilter.filter(), equalTo(false));		
		assertThat(dummyConfigProviderOut.isHeating(), equalTo(HeatPower.HEAT_POWER_0));
	}
	
	@Test
	public void heiztNichtWennTemperaturÜberLegionellenTemperatur()
	{	
		timeProvider.setDateTimeProvider( () -> LocalDateTime.of(2015, 12, 28, 4, 00) );
		
		dummyHeatTimeCalculator.setTimetoHeat(0);
		dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(66f));
		dummyConfigProvider.setLegionellenTemperature(new TemperatureImpl(65f));
		
		assertThat(legionellenFilter.filter(), equalTo(false));		
		assertThat(dummyConfigProviderOut.isHeating(), equalTo(HeatPower.HEAT_POWER_0));		
	}
	
	@Test
	public void heiztNichtWennEndTimeVorNachtheizungsend()
	{	
		timeProvider.setDateTimeProvider( () -> LocalDateTime.of(2015, 12, 28, 4, 00) );
		
		dummyHeatTimeCalculator.setTimetoHeat(120);
		dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(60f));
		dummyConfigProvider.setLegionellenTemperature(new TemperatureImpl(65f));
		
		assertThat(legionellenFilter.filter(), equalTo(false));		
		assertThat(dummyConfigProviderOut.isHeating(), equalTo(HeatPower.HEAT_POWER_0));		
	}
	
}
