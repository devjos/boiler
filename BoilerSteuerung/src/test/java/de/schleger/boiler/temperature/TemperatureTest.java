package de.schleger.boiler.temperature;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

public class TemperatureTest
{
	@Test
	public void canCompareTwoTemperatures()
	{
		Temperature temperatureImpl = new TemperatureImpl(34.45f);
		Temperature temperatureImpl2 = new TemperatureImpl(34.12f);
		
		Assert.assertThat(temperatureImpl, Matchers.greaterThan(temperatureImpl2));
	}
}
