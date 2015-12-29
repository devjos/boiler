package de.schleger.boiler.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.schleger.boiler.analyze.TemperatureAnalyzer;
import de.schleger.boiler.analyze.TemperatureAnalyzerFileImpl;
import de.schleger.boiler.boiler.BoilerController;
import de.schleger.boiler.boiler.BoilerControllerGPIOImpl;
import de.schleger.boiler.config.ConfigProviderIn;
import de.schleger.boiler.config.ConfigProviderInFileImpl;
import de.schleger.boiler.config.ConfigProviderOut;
import de.schleger.boiler.config.ConfigProviderOutFileImpl;
import de.schleger.boiler.filter.TemperatureActionFilter;
import de.schleger.boiler.filter.TemperatureActionFilterLegionellen;
import de.schleger.boiler.filter.TemperatureActionFilterNachtHeizung;
import de.schleger.boiler.heat.HeatTimeCalculator;
import de.schleger.boiler.heat.HeatTimeCalulatorImpl;
import de.schleger.boiler.heat.HeatTimeInterpolatorImpl;
import de.schleger.boiler.information.Fuellstandsanzeige;
import de.schleger.boiler.information.InformationUpdater;
import de.schleger.boiler.schedule.BoilerScheduleImpl;
import de.schleger.boiler.task.BoilerTaskImpl;
import de.schleger.boiler.time.TimeProvider;
import de.schleger.boiler.time.TimeProviderImpl;

public class BoilerMain 
{
	private static final String BOILER_CONF_IN = "/etc/boiler/boiler.config.in";
	private static final File FILE_IN = new File(BOILER_CONF_IN);
	
	private static final String BOILER_CONF_OUT = "/etc/boiler/boiler.config.out";
	private static final File FILE_OUT = new File(BOILER_CONF_OUT);
	
	private static final String TEMP_LOG = "/var/log/temperature/temp.log";
	private static final File FILE_TEMP = new File(TEMP_LOG);
	
	public static void main(String[] args) 
	{
		TimeProvider timeProviderImpl = new TimeProviderImpl();
		BoilerController boilerContoller = new BoilerControllerGPIOImpl();
		ConfigProviderIn configProviderIn = new ConfigProviderInFileImpl(FILE_IN);
		TemperatureAnalyzer temperatureAnalyzer = new TemperatureAnalyzerFileImpl(FILE_TEMP);
		ConfigProviderOut configProviderOut = new ConfigProviderOutFileImpl(FILE_OUT, boilerContoller);
		HeatTimeCalculator heatTimeCalulator = new HeatTimeCalulatorImpl(new HeatTimeInterpolatorImpl());
				
		List<TemperatureActionFilter> temperaturActionFilterList = new ArrayList<TemperatureActionFilter>();
		temperaturActionFilterList.add(new TemperatureActionFilterNachtHeizung(
						timeProviderImpl, configProviderIn, temperatureAnalyzer, 
						configProviderOut, heatTimeCalulator));
		temperaturActionFilterList.add(new TemperatureActionFilterLegionellen(
						timeProviderImpl, configProviderIn, temperatureAnalyzer, 
						configProviderOut, heatTimeCalulator));
		
		List<InformationUpdater> informationProviderList = new ArrayList<>();
		informationProviderList.add(timeProviderImpl);
		informationProviderList.add(configProviderIn);
		informationProviderList.add(temperatureAnalyzer);
		informationProviderList.add(new Fuellstandsanzeige(configProviderIn, configProviderOut, temperatureAnalyzer));
		
	    TimerTask boilerTaskImpl = new BoilerTaskImpl(new BoilerScheduleImpl(temperaturActionFilterList, informationProviderList));
	    
	    Timer timer = new Timer();
	    timer.schedule(boilerTaskImpl, 1000l, 30000l);
	}
}
