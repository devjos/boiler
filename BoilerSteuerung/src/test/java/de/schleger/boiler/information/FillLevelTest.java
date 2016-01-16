package de.schleger.boiler.information;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import de.schleger.boiler.analyze.DummyTemperatureAnalyzer;
import de.schleger.boiler.config.DummyConfigProviderIn;
import de.schleger.boiler.config.DummyConfigProviderOut;
import de.schleger.boiler.temperature.TemperatureImpl;
import de.schleger.boiler.time.DummyTimeProvider;

public class FillLevelTest 
{
	private DummyConfigProviderIn dummyConfigProviderIn;
	private DummyConfigProviderOut dummyConfigProviderOut;
	private DummyTemperatureAnalyzer dummyTemperatureAnalyzer;
	
	private FillLevel fillLevel;
	private DummyTimeProvider dummyTimeProvider;

	@Before
	public void setUp()
	{
		dummyConfigProviderIn = new DummyConfigProviderIn();
		dummyConfigProviderOut = new DummyConfigProviderOut();
		dummyTemperatureAnalyzer = new DummyTemperatureAnalyzer();
		dummyTimeProvider = new DummyTimeProvider();
		dummyTimeProvider.setTime(LocalDateTime.of(2015, 1, 1, 10, 10));
		
		fillLevel = new FillLevel(dummyConfigProviderIn, dummyConfigProviderOut, dummyTemperatureAnalyzer, dummyTimeProvider);	
	}
	
	@Test
	public void ifTemperatureIsUnderMindesttemperatureFillLevelIs0()
	{
		dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(22f));
		dummyConfigProviderIn.setTargetTemperature(new TemperatureImpl(40f));
		dummyConfigProviderIn.setEmptyTemperature(new TemperatureImpl(25f));
		
		fillLevel.update();
		
		assertThat(dummyConfigProviderOut.getFillLevel(), equalTo(0));
	}
	
	@Test
	public void ifTemperatureIsOverTargettemperatureFillLevelIs100()
	{
		dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(42f));
		dummyConfigProviderIn.setTargetTemperature(new TemperatureImpl(40f));
		dummyConfigProviderIn.setEmptyTemperature(new TemperatureImpl(25f));
		
		fillLevel.update();
		
		assertThat(dummyConfigProviderOut.getFillLevel(), equalTo(100));
	}
	
	@Test
	public void ifTemperatureIsBetweenMinAndTargetInterpolateFillLevel()
	{
		dummyConfigProviderIn.setTargetTemperature(new TemperatureImpl(40f));
		dummyConfigProviderIn.setEmptyTemperature(new TemperatureImpl(15f));
		
		dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(38f));
		fillLevel.update();		
		assertThat(dummyConfigProviderOut.getFillLevel(), equalTo(74));
		
		dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(32f));
		fillLevel.update();		
		assertThat(dummyConfigProviderOut.getFillLevel(), equalTo(57));
		
		dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(23f));
		fillLevel.update();		
		assertThat(dummyConfigProviderOut.getFillLevel(), equalTo(37));
		
		dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(18f));
		fillLevel.update();		
		assertThat(dummyConfigProviderOut.getFillLevel(), equalTo(22));
	}
	
	@Test
	public void ifTemperatureIsOverTargetInterpolateFillLevelAndHadNewTargetTillMidnight()
	{
		dummyConfigProviderIn.setTargetTemperature(new TemperatureImpl(40f));
		dummyConfigProviderIn.setEmptyTemperature(new TemperatureImpl(15f));
		
		// Neues maximum
		dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(50f));
		fillLevel.update();		
		assertThat(dummyConfigProviderOut.getFillLevel(), equalTo(100));
		
		dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(32f));
		fillLevel.update();		
		assertThat(dummyConfigProviderOut.getFillLevel(), equalTo(49));
		
		dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(23f));
		fillLevel.update();		
		assertThat(dummyConfigProviderOut.getFillLevel(), equalTo(31));
		
		dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(18f));
		fillLevel.update();		
		assertThat(dummyConfigProviderOut.getFillLevel(), equalTo(18));
		
		// Um Mitternacht wird maximum Resettet
		dummyTimeProvider.setTime(LocalDateTime.of(2015, 1, 1, 0, 10));
		
		dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(32f));
		fillLevel.update();		
		assertThat(dummyConfigProviderOut.getFillLevel(), equalTo(57));
	}
}
