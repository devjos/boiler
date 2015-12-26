package de.schleger.boiler.boiler;

import de.schleger.boiler.config.HeatPower;

public class DummyBoilerController implements BoilerController {

	private HeatPower heatPower;

	@Override
	public void setHeatPower(HeatPower heatPower) 
	{
		this.heatPower = heatPower;
	}

	public Object getHeatPower() 
	{
		return heatPower;
	}
}
