import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class HeatProvider {
	
	public static void main(String[] args){
		
		GpioController gpio  = GpioFactory.getInstance();
		
		final GpioPinDigitalOutput pin2kw = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "Relais 2KW", PinState.HIGH);
		final GpioPinDigitalOutput pin4kw = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "Relais 4KW", PinState.HIGH);
		
		//falls sich das Programm beendet wird der state auf high gesetzt
		//-->kein strom fließt
		pin2kw.setShutdownOptions(true, PinState.HIGH);
		pin4kw.setShutdownOptions(true, PinState.HIGH);
		
		while(true){
			System.out.println("set low");
			pin2kw.low();
			sleep();
			System.out.println("set high");
			pin2kw.high();
			sleep();
		}
		
	}
	
	private static void sleep(){
		try{Thread.sleep(1000);}catch ( InterruptedException e){/*ignore*/};
	}
	
}
