package de.schleger.boiler.config;

public enum ConfigKeyIn 
{
	// IN
	TARGET_TEMPERATURE,
	LEGIONELLEN_TEMPERATURE,
	EMPTY_TEMPERATURE;
	
	public static Float getDefault(ConfigKeyIn key){
		switch (key) {
		case TARGET_TEMPERATURE:
			return 40f;
			
		case LEGIONELLEN_TEMPERATURE:
			return 50f;
			
		case EMPTY_TEMPERATURE:
			return 15f;

		default:
			throw new IllegalArgumentException("Unknown config key " + key);
		}
	}
}
