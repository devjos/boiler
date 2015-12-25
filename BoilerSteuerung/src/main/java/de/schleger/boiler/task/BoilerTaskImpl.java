package de.schleger.boiler.task;

import java.util.TimerTask;

import de.schleger.boiler.schedule.BiolerSchedule;

public class BoilerTaskImpl extends TimerTask
{
	private BiolerSchedule logic;

	public BoilerTaskImpl(BiolerSchedule logic)
	{
		this.logic = logic;
	}
	
	@Override
	public void run() 
	{
		logic.analyse();
	}
}
