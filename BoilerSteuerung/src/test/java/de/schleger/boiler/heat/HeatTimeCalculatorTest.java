package de.schleger.boiler.heat;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import de.schleger.boiler.config.HeatPower;
import de.schleger.boiler.temperature.TemperatureImpl;

public class HeatTimeCalculatorTest 
{
	private HeatTimeCalulatorImpl heatTimeCalulator;

	@Before
	public void setUp()
	{
		heatTimeCalulator = new HeatTimeCalulatorImpl(new DummyHeatTimeInterpolator());		
	}
	
	@Test
	public void errechnetAusDifferenzZuTargetDieZeit()
	{				
		assertThat(heatTimeCalulator.calculate(new TemperatureImpl(new Float("20.0")), new TemperatureImpl(new Float("40")), HeatPower.HEAT_POWER_3), equalTo(55));
		assertThat(heatTimeCalulator.calculate(new TemperatureImpl(new Float("20.81")), new TemperatureImpl(new Float("48.63")), HeatPower.HEAT_POWER_2), equalTo(127));
		assertThat(heatTimeCalulator.calculate(new TemperatureImpl(new Float("20.81")), new TemperatureImpl(new Float("20.82")), HeatPower.HEAT_POWER_1), equalTo(16));
	}
	
	@Test
	public void wennNichtsZuHeizenZeit0()
	{
		assertThat(heatTimeCalulator.calculate(new TemperatureImpl(new Float("40")), new TemperatureImpl(new Float("40")), HeatPower.HEAT_POWER_1), equalTo(0));
		assertThat(heatTimeCalulator.calculate(new TemperatureImpl(new Float("50")), new TemperatureImpl(new Float("40")), HeatPower.HEAT_POWER_3), equalTo(0));
	}
}
