package de.schleger.boiler.heat;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import de.schleger.boiler.config.ConfigProviderIn;
import de.schleger.boiler.config.DummyConfigProviderIn;
import de.schleger.boiler.config.HeatPower;
import de.schleger.boiler.temperature.TemperatureImpl;

public class HeatCostCalculatorTest 
{
	private HeatCostCalculatorImpl heatCostCalculator;
	private DummyConfigProviderIn configProviderIn;

	@Before
	public void setUp()
	{
		configProviderIn = new DummyConfigProviderIn();
		configProviderIn.setEuroPerKwhDay(2f);
		configProviderIn.setEuroPerKwhNight(1f);
		heatCostCalculator = new HeatCostCalculatorImpl(configProviderIn);
	}
	
	@Test
	public void calculateCostInNight()
	{				
		LocalDateTime start = LocalDateTime.of(2015, 2, 4, 1, 00);
		LocalDateTime end = start.plusHours(1);
		float cost = heatCostCalculator.calculate(start, end, HeatPower.HEAT_POWER_1);
		assertThat(cost, equalTo(2f));
	}
	
	@Test
	public void calculateCostInDay()
	{				
		LocalDateTime start = LocalDateTime.of(2015, 2, 4, 11, 00);
		LocalDateTime end = start.plusHours(1);
		float cost = heatCostCalculator.calculate(start, end, HeatPower.HEAT_POWER_1);
		assertThat(cost, equalTo(4f));
	}
	
	@Test
	public void calculateCostNightToDay()
  	{               
  	    LocalDateTime start = LocalDateTime.of(2015, 2, 4, 05, 00);
  	    LocalDateTime end = start.plusHours(2);
  	    float cost = heatCostCalculator.calculate(start, end, HeatPower.HEAT_POWER_2);
  	    // at the moment incorrectly assumes that all heating occured in the night
  	    // usually this just happens when the heating switches off after night has ended
  	    assertThat(cost, equalTo(8f));
  	}
	
}
