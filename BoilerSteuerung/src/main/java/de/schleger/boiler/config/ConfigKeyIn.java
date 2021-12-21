package de.schleger.boiler.config;

public enum ConfigKeyIn 
{
	// IN
	TARGET_TEMPERATURE,
	LEGIONELLEN_TEMPERATURE,
	EMPTY_TEMPERATURE,
	EURO_PER_KWH_NIGHT,
	EURO_PER_KWH_DAY;
	
	public static Float getDefault(ConfigKeyIn key){
		switch (key) {
		case TARGET_TEMPERATURE:
			return 40f;
			
		case LEGIONELLEN_TEMPERATURE:
			return 50f;
			
		case EMPTY_TEMPERATURE:
			return 15f;
			
		case EURO_PER_KWH_DAY:
			return 0.185283f;
					
		case EURO_PER_KWH_NIGHT:
			return 0.216342f;

		default:
			throw new IllegalArgumentException("Unknown config key " + key);
		}
	}
}
