package de.schleger.boiler.schedule;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.schleger.boiler.filter.TemperatureActionFilter;

public class BoilerScheduleImpl implements BoilerSchedule
{	
	private static final Logger LOG = LogManager.getLogger(BoilerScheduleImpl.class);

	private List<TemperatureActionFilter> actionFilterList;
	
	private TemperatureActionFilter activeFilter;	
	private boolean isFilterInAction;

	
	public BoilerScheduleImpl(List<TemperatureActionFilter> actionFilterList) 
	{
		this.actionFilterList = actionFilterList;
	}

	@Override
	public void analyse() 
	{
		// Target abarbeiten
		if(isFilterInAction)
		{
			if(!activeFilter.filter()) 
			{
				// Wenn Filter fertig alles zur�ckdrehen
				isFilterInAction = false;
				activeFilter = null;
			}
		}
		// Neues Target ermitteln
		else
		{
			for (TemperatureActionFilter temperaturTargetFilter : actionFilterList) 
			{
				boolean localIsFilterInAction = temperaturTargetFilter.filter();
				
				if(localIsFilterInAction)
				{					
					activeFilter = temperaturTargetFilter;
					isFilterInAction = true;
					return;				
				}
			}
		}		
	}
}
