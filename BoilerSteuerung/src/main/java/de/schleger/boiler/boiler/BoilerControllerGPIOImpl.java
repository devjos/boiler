package de.schleger.boiler.boiler;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import de.schleger.boiler.config.HeatPower;

public class BoilerControllerGPIOImpl implements PropertyChangeListener 
{
	private static final Logger LOG = LogManager.getLogger(BoilerControllerGPIOImpl.class);
	
	private final GpioPinDigitalOutput pin2kw;
	private final GpioPinDigitalOutput pin4kw;

	public BoilerControllerGPIOImpl() 
	{
		GpioController gpio  = GpioFactory.getInstance();
		
		pin2kw = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "Relais 2KW", PinState.HIGH);
		pin4kw = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "Relais 4KW", PinState.HIGH);
		
		//falls sich das Programm beendet wird der state auf high gesetzt
		//-->kein strom fließt
		pin2kw.setShutdownOptions(true, PinState.HIGH);
		pin4kw.setShutdownOptions(true, PinState.HIGH);
	}

	private void setHeatPower(HeatPower heatPower) 
	{
		LOG.info("GPIO HEAT_LEVEL=" + heatPower.toString());
		
		switch (heatPower) 
		{
			case HEAT_POWER_3:
				pin2kw.low();
				pin4kw.low();			
				break;
			case HEAT_POWER_2:
				pin2kw.high();
				pin4kw.low();			
				break;
			case HEAT_POWER_1:
				pin2kw.low();
				pin4kw.high();			
				break;
			case HEAT_POWER_0:
				pin2kw.high();
				pin4kw.high();			
				break;
			default:
				pin2kw.high();
				pin4kw.high();	
			break;
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		HeatPower power = (HeatPower) evt.getNewValue();
		setHeatPower(power);
	}
}
