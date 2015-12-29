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
		
		// Temperature delta / Zeit in min
		// Stufe 2
		// max value, damit keine out of range exception möglich ist
		// 50	800	// Hand
		// 24,8 450
		// 7,0	270
		// 3,5	150
		// 0	90  // Hand
		double[] x2 = {0.0f, 3.5f, 7.0f, 24.8f, 50.0f, Float.MAX_VALUE};
		double[] y2 = {90f, 150f, 270f, 450f, 800f, Float.MAX_VALUE};
		interpolate2 = splineInterpolator.interpolate(x2, y2);
		
		
		// Stufe 3
		// max value, damit keine out of range exception möglich ist
		// 50	500	// Hand
		// 15,2 172
		// 14,0	165
		// 8,5	155
		// 2,0	90
		// 0	60  // Hand
		double[] x3 = {0f, 	2.0, 8.5f, 14.0f, 15.2f, 50.0f, Float.MAX_VALUE};
		double[] y3 = {60f, 90f, 155f, 165f, 172f, 500f, Float.MAX_VALUE};		
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
			return (float)interpolate2.value(deltaTemperature) / 2 * 3; // TODO Werte bei bedarf eintragen

		default:
			return new Float(0f);
		}		
	}	
}
