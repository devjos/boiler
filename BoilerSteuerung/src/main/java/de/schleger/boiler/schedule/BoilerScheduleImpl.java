package de.schleger.boiler.schedule;

import java.util.List;

import de.schleger.boiler.filter.TemperatureActionFilter;
import de.schleger.boiler.information.InformationUpdater;

public class BoilerScheduleImpl implements BoilerSchedule
{	
	//private static final Logger LOG = LogManager.getLogger(BoilerScheduleImpl.class);

	private final List<TemperatureActionFilter> actionFilterList;
	private final List<InformationUpdater> informationProviderList;
	
	private TemperatureActionFilter activeFilter;	

	public BoilerScheduleImpl(List<TemperatureActionFilter> actionFilterList, List<InformationUpdater> informationProviderList) 
	{
		this.actionFilterList = actionFilterList;
		this.informationProviderList = informationProviderList;
	}

	@Override
	public void analyse() 
	{
		informationProviderList.forEach( prov -> prov.update() );
		
		// Target abarbeiten
		if(activeFilter != null)
		{
			if(!activeFilter.filter()) 
			{
				// Wenn Filter fertig alles zurückdrehen
				activeFilter = null;
			}
		}
		// Neues Target ermitteln
		else
		{
			actionFilterList.stream()
				.filter( actionFilter -> actionFilter.filter() )
				.findFirst()
				.ifPresent( actionFilter -> activeFilter = actionFilter );
		}		
	}
}
