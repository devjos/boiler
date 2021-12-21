package de.schleger.boiler.heat;

import de.schleger.boiler.config.HeatPower;
import de.schleger.boiler.temperature.Temperature;

public class DummyHeatTimeCalculator implements HeatTimeCalculator {

	private int timetoHeat;
	private Temperature startTemp;
	private Temperature endTemp;
	private HeatPower heatPower;

	@Override
	public int calculate(Temperature startTemp, Temperature endTemp, HeatPower heatPower) 
	{
		this.startTemp = startTemp;
		this.endTemp = endTemp;
		this.heatPower = heatPower;

		return timetoHeat;
	}
	
	public void setTimetoHeat(int timetoHeat)
	{
		this.timetoHeat = timetoHeat;		
	}

	public Temperature getStartTemp() 
	{
		return startTemp;
	}

	public Temperature getEndTemp() 
	{
		return endTemp;
	}

	public HeatPower getHeatPower() 
	{
		return heatPower;
	}
}
