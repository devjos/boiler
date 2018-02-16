package de.schleger.boiler.heat;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.schleger.boiler.config.ConfigProviderIn;
import de.schleger.boiler.config.HeatPower;

import static de.schleger.boiler.time.BoilerTime.isNight;

public class HeatCostCalculatorImpl implements HeatCostCalculator
{
	private static final Logger LOG = LogManager.getLogger(HeatCostCalculatorImpl.class);
	
	private final ConfigProviderIn configProvider;

	public HeatCostCalculatorImpl(ConfigProviderIn configProvider) {
		this.configProvider = configProvider;
	}

	@Override
	public float calculate(LocalDateTime start, LocalDateTime end, HeatPower heatPower) {
		
		long seconds = ChronoUnit.SECONDS.between(start, end);
		
		final float kw;
		
		switch (heatPower) {
		case HEAT_POWER_0:
			kw = 0;
			break;
			
		case HEAT_POWER_1:	
			kw = 2;
			break;
			
		case HEAT_POWER_2:
			kw = 4;
			break;
			
		case HEAT_POWER_3:
			kw = 6;
			break;

		default:
			//unknwon
			kw = 0;
			break;
		}
		
		float kwh = kw * seconds / 60 / 60;
		
		final float pricePerKwh;
		if ( isNight(start) ){
			if ( isNight(end) ){
				pricePerKwh = configProvider.getEuroPerKwhNight();
			}
			else{
				LOG.warn("start in night, end in day. will take the price of the night for heating cost calculation");
				pricePerKwh = configProvider.getEuroPerKwhNight();
			}
		}
		else{
			if ( isNight(end) ){
				LOG.warn("start in day, end in night not implemented yet");
				pricePerKwh = 0;
			}
			else{
				pricePerKwh = configProvider.getEuroPerKwhDay();
			}
		}
		float eur = kwh * pricePerKwh;

		return eur;
	}
	
}
