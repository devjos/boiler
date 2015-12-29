package de.schleger.boiler.heat;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import de.schleger.boiler.config.HeatPower;

public class HeatTimeInterpolatorTest 
{
	private HeatTimeInterpolator heatTimeInterpolator;

	@Before
	public void setUp()
	{
		heatTimeInterpolator = new HeatTimeInterpolatorImpl();
	}
	
	@Test
	public void canInterplolateHeizstufe1()
	{		
		assertThat(heatTimeInterpolator.interpolateTimeToHeat(6.0f, HeatPower.HEAT_POWER_1), equalTo(351.68256F));
		assertThat(heatTimeInterpolator.interpolateTimeToHeat(26.81f, HeatPower.HEAT_POWER_1), equalTo(689.6192F));
	}
	
	@Test
	public void canInterplolateHeizstufe2()
	{		
		assertThat(heatTimeInterpolator.interpolateTimeToHeat(2.5f, HeatPower.HEAT_POWER_2), equalTo(126.78576F));
		assertThat(heatTimeInterpolator.interpolateTimeToHeat(6.0f, HeatPower.HEAT_POWER_2), equalTo(234.45503F));
		assertThat(heatTimeInterpolator.interpolateTimeToHeat(17.2f, HeatPower.HEAT_POWER_2), equalTo(433.41953F));
		assertThat(heatTimeInterpolator.interpolateTimeToHeat(26.81f, HeatPower.HEAT_POWER_2), equalTo(459.74612F));
	}
	
	@Test
	public void canInterplolateHeizstufe3()
	{		
		assertThat(heatTimeInterpolator.interpolateTimeToHeat(2.5f, HeatPower.HEAT_POWER_3), equalTo(97.117134F));
		assertThat(heatTimeInterpolator.interpolateTimeToHeat(6.0f, HeatPower.HEAT_POWER_3), equalTo(138.52592F));
		assertThat(heatTimeInterpolator.interpolateTimeToHeat(14.0f, HeatPower.HEAT_POWER_3), equalTo(165.0F));
		assertThat(heatTimeInterpolator.interpolateTimeToHeat(17.2f, HeatPower.HEAT_POWER_3), equalTo(185.47919F));
		assertThat(heatTimeInterpolator.interpolateTimeToHeat(26.81f, HeatPower.HEAT_POWER_3), equalTo(262.5162F));
	}

}
