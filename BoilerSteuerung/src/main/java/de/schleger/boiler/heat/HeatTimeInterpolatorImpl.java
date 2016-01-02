package de.schleger.boiler.heat;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import de.schleger.boiler.config.HeatPower;

public class HeatTimeInterpolatorImpl implements HeatTimeInterpolator
{
	private PolynomialSplineFunction interpolate2;
	private PolynomialSplineFunction interpolate3;
	
	public HeatTimeInterpolatorImpl() 
	{
		SplineInterpolator splineInterpolator = new SplineInterpolator();
		
		// TODO werte für Stufe sind alle nur geschätz und müssen entsprechend bei verwendung nachgetragen werden
		// Temperature delta / Zeit in min
		// Stufe 2
		// 50	800	// OLD
		// 24,8 450 // OLD
		// 7,0	270 // OLD
		// 3,5	150 // OLD
		// 0	90  // OLD
		double[] x2 = {0.0f, 3.5f, 7.0f, 24.8f, 50.0f, Float.MAX_VALUE};
		double[] y2 = {90f, 150f, 270f, 450f, 800f, Float.MAX_VALUE};
		interpolate2 = splineInterpolator.interpolate(x2, y2);
		
		
		// Stufe 3 (ungefähr 12 Grad Pro Stunde steigung ab NullPunkt)
		// 55	320	// OLD
		// 15,2 130 // OLD
		// 14,0	120 // OLD
		// 12,0 110
		// 9,2	100
		// 7,2	85
		// 3,0	65
		// 2,0	60 	// OLD
		// 0	50 
		double[] x3 = {0f, 	2.0, 3.0, 7.2f, 9.2f, 12.0f, 14.0f, 15.2f, 55.0f, Float.MAX_VALUE};
		double[] y3 = {50f, 60f, 65f, 85f,  100f,  110f,  120f,  130f,  320f, Float.MAX_VALUE};		
		interpolate3 = splineInterpolator.interpolate(x3, y3);
		
	}

	@Override
	public Float interpolateTimeToHeat(float deltaTemperature, HeatPower heatPower) 
	{
		switch (heatPower) 
		{
			case HEAT_POWER_3:			
			return (float)interpolate3.value(deltaTemperature);
			
			case HEAT_POWER_2:
			return (float)interpolate2.value(deltaTemperature);
			
			case HEAT_POWER_1:
			return (float)interpolate2.value(deltaTemperature) / 2 * 3; // TODO Werte bei bedarf für Stufe 1 eintragen

		default:
			return new Float(0f);
		}		
	}	
}
