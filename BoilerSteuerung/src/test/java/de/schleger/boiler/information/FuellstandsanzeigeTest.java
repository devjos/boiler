package de.schleger.boiler.information;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import de.schleger.boiler.analyze.DummyTemperatureAnalyzer;
import de.schleger.boiler.config.DummyConfigProviderIn;
import de.schleger.boiler.config.DummyConfigProviderOut;
import de.schleger.boiler.temperature.TemperatureImpl;

public class FuellstandsanzeigeTest 
{
	private DummyConfigProviderIn dummyConfigProviderIn;
	private DummyConfigProviderOut dummyConfigProviderOut;
	private DummyTemperatureAnalyzer dummyTemperatureAnalyzer;
	
	private Fuellstandsanzeige fuellstandsanzeige;

	@Before
	public void setUp()
	{
		dummyConfigProviderIn = new DummyConfigProviderIn();
		dummyConfigProviderOut = new DummyConfigProviderOut();
		dummyTemperatureAnalyzer = new DummyTemperatureAnalyzer();
		
		fuellstandsanzeige = new Fuellstandsanzeige(dummyConfigProviderIn, dummyConfigProviderOut, dummyTemperatureAnalyzer);	
	}
	
	@Test
	public void ifTemperatureIsUnderMindesttemperatureFillLevelIs0()
	{
		dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(22f));
		dummyConfigProviderIn.setTargetTemperature(new TemperatureImpl(40f));
		dummyConfigProviderIn.setEmptyTemperature(new TemperatureImpl(25f));
		
		fuellstandsanzeige.update();
		
		assertThat(dummyConfigProviderOut.getFillLevel(), equalTo(0));
	}
	
	@Test
	public void ifTemperatureIsOverTargettemperatureFillLevelIs100()
	{
		dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(42f));
		dummyConfigProviderIn.setTargetTemperature(new TemperatureImpl(40f));
		dummyConfigProviderIn.setEmptyTemperature(new TemperatureImpl(25f));
		
		fuellstandsanzeige.update();
		
		assertThat(dummyConfigProviderOut.getFillLevel(), equalTo(100));
	}
	
	@Test
	public void ifTemperatureIsBetweenMinAndTargetInterpolateFillLevel()
	{
		dummyConfigProviderIn.setTargetTemperature(new TemperatureImpl(40f));
		dummyConfigProviderIn.setEmptyTemperature(new TemperatureImpl(15f));
		
		dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(32f));
		fuellstandsanzeige.update();		
		assertThat(dummyConfigProviderOut.getFillLevel(), equalTo(57));
		
		dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(23f));
		fuellstandsanzeige.update();		
		assertThat(dummyConfigProviderOut.getFillLevel(), equalTo(38));
		
		dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(18f));
		fuellstandsanzeige.update();		
		assertThat(dummyConfigProviderOut.getFillLevel(), equalTo(22));
	}
}
