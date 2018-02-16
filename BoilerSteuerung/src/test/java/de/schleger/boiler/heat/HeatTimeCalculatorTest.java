package de.schleger.boiler.heat;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import de.schleger.boiler.config.HeatPower;
import de.schleger.boiler.temperature.TemperatureImpl;

public class HeatTimeCalculatorTest 
{
	private HeatTimeCalculatorImpl heatTimeCalculator;

	@Before
	public void setUp()
	{
		heatTimeCalculator = new HeatTimeCalculatorImpl(new DummyHeatTimeInterpolator());		
	}
	
	@Test
	public void errechnetAusDifferenzZuTargetDieZeitUndAddiertEinenPuffer()
	{				
		assertThat(heatTimeCalculator.calculate(new TemperatureImpl(new Float(20.0f)), new TemperatureImpl(new Float(40f)), HeatPower.HEAT_POWER_3), equalTo(60));
		assertThat(heatTimeCalculator.calculate(new TemperatureImpl(new Float(20.81f)), new TemperatureImpl(new Float(48.63f)), HeatPower.HEAT_POWER_2), equalTo(132));
		assertThat(heatTimeCalculator.calculate(new TemperatureImpl(new Float(20.81f)), new TemperatureImpl(new Float(20.82f)), HeatPower.HEAT_POWER_1), equalTo(21));
		assertThat(heatTimeCalculator.calculate(new TemperatureImpl(new Float(39.75f)), new TemperatureImpl(new Float(40f)), HeatPower.HEAT_POWER_1), equalTo(22));
	}
	
	@Test
	public void wennNichtsZuHeizenZeit0()
	{
		assertThat(heatTimeCalculator.calculate(new TemperatureImpl(new Float(40f)), new TemperatureImpl(new Float(40f)), HeatPower.HEAT_POWER_1), equalTo(0));
		assertThat(heatTimeCalculator.calculate(new TemperatureImpl(new Float(50f)), new TemperatureImpl(new Float(40f)), HeatPower.HEAT_POWER_3), equalTo(0));
	}
}
