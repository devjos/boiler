package de.schleger.boiler.information;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.schleger.boiler.analyze.TemperatureAnalyzer;
import de.schleger.boiler.config.ConfigProviderIn;
import de.schleger.boiler.model.BoilerModel;
import de.schleger.boiler.time.TimeProvider;

public class FillLevel implements InformationUpdater
{	
	private static final Logger LOG = LogManager.getLogger(FillLevel.class);
	
	private static final float HEAT_TEMPERATURE_DELTA = 1.00f;
	private PolynomialSplineFunction interpolate;
	
	private ConfigProviderIn configProviderIn;
	private BoilerModel boilerModel;
	private TemperatureAnalyzer temperatureAnalyzer;
	private TimeProvider timeProvider;

	private float target;

	public FillLevel(ConfigProviderIn configProviderIn, BoilerModel boilerModel, TemperatureAnalyzer temperatureAnalyzer, TimeProvider timeProvider) 
	{
		this.configProviderIn = configProviderIn;
		this.boilerModel = boilerModel;
		this.temperatureAnalyzer = temperatureAnalyzer;
		this.timeProvider = timeProvider;
		
        double[] real =         {0.0, 1.0, 03.0, 06.0, 10.0, 15.0, 21.0, 28.0, 36.0, 45.0, 50.0, 61.0, 69.0, 76.0, 84.0, 89.0, 93.0, 96.0, 98.0, 99.0, 100.0, Float.MAX_VALUE};
        double[] estimatet =	{0.0, 5.0, 10.0, 15.0, 20.0, 25.0, 30.0, 35.0, 40.0, 45.0, 50.0, 55.0, 60.0, 65.0, 70.0, 75.0, 80.0, 85.0, 90.0, 95.0, 100.0, 100.0};
        interpolate = new SplineInterpolator().interpolate(real, estimatet);		
	}
	
	@Override
	public void update() 
	{
        float min = configProviderIn.getEmptyTemperature().getTemperature();
        float ist = temperatureAnalyzer.getAverageTemperature().getTemperature();
        float target = getTargetForTheDay(ist);        
        
        if(ist < min)
        {
        	// Fuellstand 0 Prozent
        	tellConfigProviderFillLevel(0);
            return;
        }
        
        try 
        {
        	float onePercent = (target - min) / 100f;        
        	float fuellstandPercentage = (ist - min) / onePercent;
        	
        	float fillLevel = (float)interpolate.value(fuellstandPercentage);
        	if(fillLevel > 100f)
        	{
        		fillLevel = 100f;
        	}
        	
        	tellConfigProviderFillLevel(fillLevel);			
		} 
        catch (Exception e) 
        {
    		LOG.error("Fehler beim ermitteln des FillLevel", e);
		}
	}

	/**
	 * Wenn das Target für den Tag sammt Puffer überschritten wird, 
	 * dann wird von einem neuen maximum bis in die Nacht ausgegangen.
	 * 
	 * Dann wird das Tagesmaximum wieder resettet und die Config greift,
	 * bis sie wieder überschritten wird
	 */
	private float getTargetForTheDay(float ist) 
	{	
		float configTarget = configProviderIn.getTargetTemperature().getTemperature() + HEAT_TEMPERATURE_DELTA;		
		
		// DayTarget resetten in der Nacht bzw. initalisieren
		if(timeProvider.getTime().getHour() < 1 || target < configTarget)
		{
			target = configTarget;
		}
       
		// DayTarget anpassen
        if(ist > target)
        {
        	target = ist;
        }
        
		return target;
	}

	private void tellConfigProviderFillLevel(float fillLevel) 
	{
		LOG.info("FILL_LEVEL=" + fillLevel);
		boilerModel.setFillLevel(Math.round(fillLevel));
	}	
}