package de.schleger.boiler.config;

public enum ConfigKeyIn 
{
	// IN
	TARGET_TEMPERATURE,
	LEGIONELLEN_TEMPERATURE;
	
	public static Float getDefault(ConfigKeyIn key){
		switch (key) {
		case TARGET_TEMPERATURE:
			return 39f;
			
		case LEGIONELLEN_TEMPERATURE:
			return 65f;

		default:
			throw new IllegalArgumentException("Unknown config key " + key);
		}
	}
}
