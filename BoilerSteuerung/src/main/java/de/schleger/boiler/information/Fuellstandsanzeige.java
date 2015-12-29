package de.schleger.boiler.information;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import de.schleger.boiler.analyze.TemperatureAnalyzer;
import de.schleger.boiler.config.ConfigProviderIn;
import de.schleger.boiler.config.ConfigProviderOut;

public class Fuellstandsanzeige implements InformationUpdater
{
	private ConfigProviderIn configProviderIn;
	private ConfigProviderOut configProviderOut;
	private TemperatureAnalyzer temperatureAnalyzer;

	public Fuellstandsanzeige(ConfigProviderIn configProviderIn, ConfigProviderOut configProviderOut, TemperatureAnalyzer temperatureAnalyzer) 
	{
		this.configProviderIn = configProviderIn;
		this.configProviderOut = configProviderOut;
		this.temperatureAnalyzer = temperatureAnalyzer;
	}
	
	@Override
	public void update() 
	{       
        float target = configProviderIn.getTargetTemperature().getTemperature();
        float min = configProviderIn.getEmptyTemperature().getTemperature();
        float ist = temperatureAnalyzer.getAverageTemperature().getTemperature();
        
        if(ist < min)
        {
        	// Fuellstand 0 Prozent
            configProviderOut.setFillLevel(0);
            return;
        }
        if(ist > target)
        {
            // Fuellstand 100 Prozent
            configProviderOut.setFillLevel(100);
            return;
        }        
        
        float onePercent = (target - min) / 100f;        
        float fuellstandPercentage = (ist - min) / onePercent;                
        
        double[] real =         {0.0, 1.0, 03.0, 06.0, 10.0, 15.0, 21.0, 28.0, 36.0, 45.0, 50.0, 64.0, 72.5, 80.0, 86.0, 90.0, 92.5, 96.0, 97.5, 99.5, 100.0};
        double[] estimatet =	{0.0, 5.0, 10.0, 15.0, 20.0, 25.0, 30.0, 35.0, 40.0, 45.0, 50.0, 55.0, 60.0, 65.0, 70.0, 75.0, 80.0, 85.0, 90.0, 95.0, 100.0};
        
        PolynomialSplineFunction interpolate = new SplineInterpolator().interpolate(real, estimatet);
            
        configProviderOut.setFillLevel(Math.round((float)interpolate.value(fuellstandPercentage)));
	}
	
}
