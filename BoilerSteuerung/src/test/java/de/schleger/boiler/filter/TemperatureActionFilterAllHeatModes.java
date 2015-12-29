package de.schleger.boiler.filter;

import de.schleger.boiler.config.ConfigProviderOut;
import de.schleger.boiler.config.HeatPower;

/**
 * Vorsicht nur für Testzwecke der Schaltung verwenden
 * @Deprecated
 */
@Deprecated
public class TemperatureActionFilterAllHeatModes implements TemperatureActionFilter {

	private ConfigProviderOut configProviderOut;

	public TemperatureActionFilterAllHeatModes(ConfigProviderOut configProviderOut) 
	{
		this.configProviderOut = configProviderOut;
	}

	@Override
	public boolean filter() 
	{
		if(configProviderOut.isHeating() == null)
		{
			configProviderOut.setHeatPower(HeatPower.HEAT_POWER_0);			
		}
		
		switch (configProviderOut.isHeating()) 
		{
			case HEAT_POWER_0:
			configProviderOut.setHeatPower(HeatPower.HEAT_POWER_1);
			break;
			case HEAT_POWER_1:
			configProviderOut.setHeatPower(HeatPower.HEAT_POWER_2);
			break;
			case HEAT_POWER_2:
			configProviderOut.setHeatPower(HeatPower.HEAT_POWER_3);
			break;
			case HEAT_POWER_3:
			configProviderOut.setHeatPower(HeatPower.HEAT_POWER_0);
			break;
		}
		
		return true;
	}

}
