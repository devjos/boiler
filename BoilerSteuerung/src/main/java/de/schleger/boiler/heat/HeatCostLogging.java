package de.schleger.boiler.heat;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.schleger.boiler.config.HeatPower;
import de.schleger.boiler.time.TimeProvider;

public class HeatCostLogging implements PropertyChangeListener {
	
	private static final Logger LOG = LogManager.getLogger(HeatCostLogging.class);
	
	private final TimeProvider timeProvider;
	private LocalDateTime timestamp;
	private final HeatCostCalculator calculator;
	
	public HeatCostLogging(TimeProvider timeProvider, HeatCostCalculator calculator) {
		this.timeProvider = timeProvider;
		this.calculator = calculator;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		HeatPower oldHeatPower = (HeatPower) evt.getOldValue();
		
		LocalDateTime oldDate = this.timestamp;
		this.timestamp = timeProvider.getTime();
		
		if ( oldDate != null && this.timestamp != null && oldHeatPower != null ){
			float costs = calculator.calculate(oldDate, this.timestamp, oldHeatPower);
			LOG.info(String.format("%.2f", costs));
		}
		 
	}

}
