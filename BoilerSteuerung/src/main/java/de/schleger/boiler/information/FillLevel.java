package de.schleger.boiler.information;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.schleger.boiler.analyze.TemperatureAnalyzer;
import de.schleger.boiler.config.ConfigProviderIn;
import de.schleger.boiler.config.ConfigProviderOut;

public class FillLevel implements InformationUpdater
{	
	private static final Logger LOG = LogManager.getLogger(FillLevel.class);
	
	private static final float HEAT_TEMPERATURE_DELTA = 0.75f;
	
	private ConfigProviderIn configProviderIn;
	private ConfigProviderOut configProviderOut;
	private TemperatureAnalyzer temperatureAnalyzer;

	public FillLevel(ConfigProviderIn configProviderIn, ConfigProviderOut configProviderOut, TemperatureAnalyzer temperatureAnalyzer) 
	{
		this.configProviderIn = configProviderIn;
		this.configProviderOut = configProviderOut;
		this.temperatureAnalyzer = temperatureAnalyzer;
	}
	
	@Override
	public void update() 
	{
		// Da die TargetTemperatur beim heizen überschritten wird, 
		// wird noch ein TemperaturDelta addiert um einen genaueren Füllstand zu erzeugen		
        float target = configProviderIn.getTargetTemperature().getTemperature() + HEAT_TEMPERATURE_DELTA;
        float min = configProviderIn.getEmptyTemperature().getTemperature();
        float ist = temperatureAnalyzer.getAverageTemperature().getTemperature();
        
        if(ist < min)
        {
        	// Fuellstand 0 Prozent
        	tellConfigProviderFillLevel(0);
            return;
        }
        if(ist > target)
        {
            // Fuellstand 100 Prozent
        	tellConfigProviderFillLevel(100);
            return;
        }        
        
        float onePercent = (target - min) / 100f;        
        float fuellstandPercentage = (ist - min) / onePercent;                
        
        double[] real =         {0.0, 1.0, 03.0, 06.0, 10.0, 15.0, 21.0, 28.0, 36.0, 45.0, 50.0, 61.0, 69.0, 76.0, 84.0, 89.0, 93.0, 96.0, 98.0, 99.0, 100.0};
        double[] estimatet =	{0.0, 5.0, 10.0, 15.0, 20.0, 25.0, 30.0, 35.0, 40.0, 45.0, 50.0, 55.0, 60.0, 65.0, 70.0, 75.0, 80.0, 85.0, 90.0, 95.0, 100.0};
        
        PolynomialSplineFunction interpolate = new SplineInterpolator().interpolate(real, estimatet);        
        int fillLevel = Math.round((float)interpolate.value(fuellstandPercentage));
        
        tellConfigProviderFillLevel(fillLevel);
	}

	private void tellConfigProviderFillLevel(int fillLevel) 
	{
		LOG.info("FILL_LEVEL=" + fillLevel);
		configProviderOut.setFillLevel(fillLevel);
	}	
}
