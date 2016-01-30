package de.schleger.boiler.filter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import de.schleger.boiler.analyze.DummyTemperatureAnalyzer;
import de.schleger.boiler.analyze.TemperatureAnalyzer;
import de.schleger.boiler.config.HeatPower;
import de.schleger.boiler.heat.DummyHeatTimeCalculator;
import de.schleger.boiler.heat.HeatTimeCalculator;
import de.schleger.boiler.model.BoilerModel;
import de.schleger.boiler.temperature.Temperature;
import de.schleger.boiler.temperature.TemperatureImpl;
import de.schleger.boiler.time.DummyTimeProvider;
import de.schleger.boiler.time.TimeProvider;

public class AbstractTemperatureActionFilterNachtHeizungTest 
{
	private DummyTimeProvider dummyTimeProvider;
	private DummyTemperatureAnalyzer dummyTemperatureAnalyzer;
	private BoilerModel boilerModel;
	private DummyHeatTimeCalculator dummyHeatTimeCalculator;
	
	private AbstractTemperaturActionFilterNachtHeizungExtension abstractActionFilter;
	
	@Before
	public void setUp()
	{
		dummyTimeProvider = new DummyTimeProvider();
		dummyTemperatureAnalyzer = new DummyTemperatureAnalyzer();
		boilerModel = new BoilerModel();
		dummyHeatTimeCalculator = new DummyHeatTimeCalculator();
		abstractActionFilter = new AbstractTemperaturActionFilterNachtHeizungExtension(dummyTimeProvider, boilerModel, dummyTemperatureAnalyzer, dummyHeatTimeCalculator);
	}

	
	@Test
	public void noHeatingOnDay()
	{
		dummyTimeProvider.setNight(false);
		dummyTimeProvider.setTime(LocalDateTime.of(2010, 8, 5, 10, 0));
		
		assertThat(abstractActionFilter.filter(), equalTo(false));
		assertThat(boilerModel.getHeatPower(), equalTo(HeatPower.HEAT_POWER_0));		
	}
	
	@Test
	public void exitWhenEndTimeIsMoreThan12HoursAway()
	{
		dummyTimeProvider.setNight(true);
		dummyTimeProvider.setTime(LocalDateTime.of(2010, 8, 5, 10, 0));
		abstractActionFilter.setEndTime(LocalDateTime.of(2010, 8, 5, 22, 1));
		
		assertThat(abstractActionFilter.filter(), equalTo(false));
		assertThat(boilerModel.getHeatPower(), equalTo(HeatPower.HEAT_POWER_0));		
	}
	
	@Test
	public void noHeatIfNoHeatingTime()
	{			
		dummyTimeProvider.setNight(true);
		dummyTimeProvider.setTime(LocalDateTime.of(2010, 8, 5, 0, 0));
		dummyHeatTimeCalculator.setTimetoHeat(0);
		dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(60f));
		abstractActionFilter.setTargetTemperature(new TemperatureImpl(50f));
		abstractActionFilter.setEndTime(LocalDateTime.of(2010, 8, 5, 6, 0));
		
		assertThat(abstractActionFilter.filter(), equalTo(false));		
		assertThat(dummyHeatTimeCalculator.getStartTemp().getTemperature(), equalTo(60f));		
		assertThat(dummyHeatTimeCalculator.getEndTemp().getTemperature(), equalTo(50f));		
		assertThat(dummyHeatTimeCalculator.getHeatPower(), equalTo(HeatPower.HEAT_POWER_3));		
		assertThat(boilerModel.getHeatPower(), equalTo(HeatPower.HEAT_POWER_0));		
	}
	
	@Test
	public void waitsForHeatingEndTime()
	{		
		LocalDateTime timecalculated = LocalDateTime.of(2010, 8, 5, 21, 0);
		LocalDateTime endHeizung = LocalDateTime.of(2010, 8, 5, 22, 0);
		
		dummyTimeProvider.setNight(true);
		dummyTimeProvider.setTime(LocalDateTime.of(2010, 8, 5, 15, 0));
		dummyHeatTimeCalculator.setTimetoHeat(60);
		dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(60f));
		abstractActionFilter.setTargetTemperature(new TemperatureImpl(50f));
		abstractActionFilter.setEndTime(endHeizung);
		dummyTimeProvider.setDateAddMinutesToTime(timecalculated);
		
		assertThat(abstractActionFilter.filter(), equalTo(false));		
		assertThat(boilerModel.getHeatPower(), equalTo(HeatPower.HEAT_POWER_0));		
	}
	
	@Test
	public void heatingEndTimeIsOver()
	{
		LocalDateTime timecalculated = LocalDateTime.of(2010, 8, 5, 23, 0);
		LocalDateTime endHeizung = LocalDateTime.of(2010, 8, 5, 22, 0);
		
		dummyTimeProvider.setNight(true);
		dummyTimeProvider.setTime(LocalDateTime.of(2010, 8, 5, 15, 0));
		dummyHeatTimeCalculator.setTimetoHeat(60);
		dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(60f));
		abstractActionFilter.setTargetTemperature(new TemperatureImpl(50f));
		abstractActionFilter.setEndTime(endHeizung);
		dummyTimeProvider.setDateAddMinutesToTime(timecalculated);
		
		assertThat(abstractActionFilter.filter(), equalTo(true));		
		assertThat(boilerModel.getHeatPower(), equalTo(HeatPower.HEAT_POWER_3));		
	}
	
	private final class AbstractTemperaturActionFilterNachtHeizungExtension
	extends AbstractTemperaturActionFilterNachtHeizung 
	{
		private LocalDateTime time;
		private Temperature temperature;

		private AbstractTemperaturActionFilterNachtHeizungExtension(TimeProvider timeProvider,
		BoilerModel boilerModel, TemperatureAnalyzer temperatureAnalyzer,
		HeatTimeCalculator heatTimeCalculator) 
	{
		super(timeProvider, boilerModel, temperatureAnalyzer, heatTimeCalculator);
	}
	
	@Override
	Temperature getTargetTemperature() {
		return temperature;
	}
	
	@Override
	LocalDateTime getEndTime() {
		return time;
	}
	
	void setTargetTemperature(Temperature temperature)
	{
		this.temperature = temperature;		
	}
	
	void setEndTime(LocalDateTime time)
	{
		this.time = time;
	}	
	}
}