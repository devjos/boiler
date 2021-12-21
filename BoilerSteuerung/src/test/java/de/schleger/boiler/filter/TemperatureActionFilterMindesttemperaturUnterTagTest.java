package de.schleger.boiler.filter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import de.schleger.boiler.analyze.DummyTemperatureAnalyzer;
import de.schleger.boiler.temperature.TemperatureImpl;
import de.schleger.boiler.time.DummyTimeProvider;

public class TemperatureActionFilterMindesttemperaturUnterTagTest {
    private TemperatureActionFilterMindesttemperaturUnterTag mindesttemperaturFilter;
    private DummyTemperatureAnalyzer dummyTemperatureProvider;
    private DummyTimeProvider dummyTimeProvider;

    @Before
    public void setUp() {
        dummyTimeProvider = new DummyTimeProvider();
        dummyTemperatureProvider = new DummyTemperatureAnalyzer();
        mindesttemperaturFilter = new TemperatureActionFilterMindesttemperaturUnterTag(dummyTemperatureProvider,
                dummyTimeProvider);
    }

    @Test
    public void temperaturUeberMindesttemperatur() {
        dummyTimeProvider.setNight(false);
        dummyTemperatureProvider.setTemperature(new TemperatureImpl(55.76f));
        assertThat(mindesttemperaturFilter.filter(), equalTo(false));
    }

    @Test
    public void temperaturUnterMindesttemperaturAberTag() {
        dummyTimeProvider.setNight(false);
        dummyTemperatureProvider.setTemperature(new TemperatureImpl(17.76f));
        assertThat(mindesttemperaturFilter.filter(), equalTo(true));
    }

    @Test
    public void temperaturUnterMindesttemperaturInNacht() {
        dummyTimeProvider.setNight(true);
        dummyTemperatureProvider.setTemperature(new TemperatureImpl(14.76f));
        assertThat(mindesttemperaturFilter.filter(), equalTo(false));
    }
}
