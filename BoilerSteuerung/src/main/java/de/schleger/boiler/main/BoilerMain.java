package de.schleger.boiler.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.schleger.boiler.analyze.TemperatureAnalyzer;
import de.schleger.boiler.analyze.TemperatureAnalyzerFileImpl;
import de.schleger.boiler.boiler.BoilerControllerGPIOImpl;
import de.schleger.boiler.config.ConfigProviderIn;
import de.schleger.boiler.config.ConfigProviderInFileImpl;
import de.schleger.boiler.config.ConfigProviderOutFileImpl;
import de.schleger.boiler.filter.TemperatureActionFilter;
import de.schleger.boiler.filter.TemperatureActionFilterNachtHeizungLegionellen;
import de.schleger.boiler.filter.TemperatureActionFilterNachtHeizungNormal;
import de.schleger.boiler.heat.HeatCostCalculatorImpl;
import de.schleger.boiler.heat.HeatCostLogging;
import de.schleger.boiler.heat.HeatTimeCalculator;
import de.schleger.boiler.heat.HeatTimeCalculatorImpl;
import de.schleger.boiler.heat.HeatTimeInterpolatorImpl;
import de.schleger.boiler.information.FillLevel;
import de.schleger.boiler.information.InformationUpdater;
import de.schleger.boiler.log.LogDescriptor;
import de.schleger.boiler.log.LogFileChecker;
import de.schleger.boiler.model.BoilerModel;
import de.schleger.boiler.schedule.BoilerScheduleImpl;
import de.schleger.boiler.task.BoilerTaskImpl;
import de.schleger.boiler.time.TimeProvider;
import de.schleger.boiler.time.TimeProviderImpl;

public class BoilerMain {
    private static final int MAX_LOG_FILE_ALIVE_TIME_IN_DAYS = 366;

    private static final String LOG_DIRECTORY = "/var/log/boiler/";
    private static final File FILE_LOG_DIRECTORY = new File(LOG_DIRECTORY);

    private static final String BOILER_LOG = "boiler.log";
    private static final String FILL_LEVEL_LOG = "fillLevel.log";
    private static final String COSTS_LOG = "costs.log";

    private static final String BOILER_CONF_IN = "/etc/boiler/boiler.config.in";
    private static final File FILE_IN = new File(BOILER_CONF_IN);

    private static final String BOILER_CONF_OUT = "/etc/boiler/boiler.config.out";
    private static final File FILE_OUT = new File(BOILER_CONF_OUT);

    private static final String TEMP_LOG = "/var/log/temperature/temp.log";
    private static final File FILE_TEMP = new File(TEMP_LOG);

    public static void main(String[] args) {
        BoilerModel boiler = new BoilerModel();
        TimeProvider timeProvider = new TimeProviderImpl();
        boiler.addPropertyChangeListener("heatPower", new BoilerControllerGPIOImpl());
        ConfigProviderIn configProviderIn = new ConfigProviderInFileImpl(FILE_IN);
        boiler.addPropertyChangeListener("heatPower",
                new HeatCostLogging(timeProvider, new HeatCostCalculatorImpl(configProviderIn)));
        TemperatureAnalyzer temperatureAnalyzer = new TemperatureAnalyzerFileImpl(FILE_TEMP);
        boiler.addPropertyChangeListener(new ConfigProviderOutFileImpl(FILE_OUT));
        HeatTimeCalculator heatTimeCalulator = new HeatTimeCalculatorImpl(new HeatTimeInterpolatorImpl());

        List<TemperatureActionFilter> temperaturActionFilterList = new ArrayList<TemperatureActionFilter>();
        temperaturActionFilterList.add(new TemperatureActionFilterNachtHeizungNormal(timeProvider, configProviderIn,
                temperatureAnalyzer, boiler, heatTimeCalulator));
        temperaturActionFilterList.add(new TemperatureActionFilterNachtHeizungLegionellen(timeProvider,
                configProviderIn, temperatureAnalyzer, boiler, heatTimeCalulator));

        List<InformationUpdater> informationProviderList = new ArrayList<>();
        informationProviderList.add(timeProvider);
        informationProviderList.add(configProviderIn);
        informationProviderList.add(temperatureAnalyzer);
        informationProviderList.add(new FillLevel(configProviderIn, boiler, temperatureAnalyzer, timeProvider));
        informationProviderList.add(new LogFileChecker(timeProvider, FILE_LOG_DIRECTORY,
                MAX_LOG_FILE_ALIVE_TIME_IN_DAYS, new LogDescriptor(BOILER_LOG, 32),
                new LogDescriptor(FILL_LEVEL_LOG, 32), new LogDescriptor(COSTS_LOG, 10 * 365 + 1))); // 10 years

        TimerTask boilerTaskImpl = new BoilerTaskImpl(
                new BoilerScheduleImpl(temperaturActionFilterList, informationProviderList));

        Timer timer = new Timer();
        timer.schedule(boilerTaskImpl, 1000l, 30000l);
    }
}