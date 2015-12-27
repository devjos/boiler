package de.schleger.boiler.temperature;

public class TemperatureImpl implements Temperature 
{
	private final Float temperature;

	public TemperatureImpl(Float temperature)
	{
		this.temperature = temperature;
	}
	
	public Float getTemperature() 
	{		
		return temperature;
	}

	@Override
	public int compareTo(Temperature t) 
	{
		return getTemperature().compareTo(t.getTemperature());
	}
	
	@Override
	public boolean equals(Object obj) 
	{
		if(obj instanceof Temperature)
		{
			Float a = temperature;
			Float temperature2 = ((Temperature) obj).getTemperature();			
			
			return a.equals(temperature2);
		}
		
		return super.equals(obj);
	}
	
	@Override
	public String toString(){
		return temperature.toString();
	}
}
