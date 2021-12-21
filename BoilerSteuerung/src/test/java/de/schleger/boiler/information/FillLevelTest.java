package de.schleger.boiler.information;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import de.schleger.boiler.analyze.DummyTemperatureAnalyzer;
import de.schleger.boiler.config.DummyConfigProviderIn;
import de.schleger.boiler.model.BoilerModel;
import de.schleger.boiler.temperature.TemperatureImpl;
import de.schleger.boiler.time.DummyTimeProvider;

public class FillLevelTest {
    private DummyConfigProviderIn dummyConfigProviderIn;
    private BoilerModel boilerModel;
    private DummyTemperatureAnalyzer dummyTemperatureAnalyzer;

    private FillLevel fillLevel;
    private DummyTimeProvider dummyTimeProvider;

    @Before
    public void setUp() {
        dummyConfigProviderIn = new DummyConfigProviderIn();
        boilerModel = new BoilerModel();
        dummyTemperatureAnalyzer = new DummyTemperatureAnalyzer();
        dummyTimeProvider = new DummyTimeProvider();
        dummyTimeProvider.setTime(LocalDateTime.of(2015, 1, 1, 10, 10));

        fillLevel = new FillLevel(dummyConfigProviderIn, boilerModel, dummyTemperatureAnalyzer, dummyTimeProvider);
    }

    @Test
    public void ifTemperatureIsUnderMindesttemperatureFillLevelIs0() {
        dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(22f));
        dummyConfigProviderIn.setTargetTemperature(new TemperatureImpl(40f));
        dummyConfigProviderIn.setEmptyTemperature(new TemperatureImpl(25f));

        fillLevel.update();

        assertThat(boilerModel.getFillLevel(), equalTo(0F));
    }

    @Test
    public void ifTemperatureIsOverTargettemperatureFillLevelIs100() {
        dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(42f));
        dummyConfigProviderIn.setTargetTemperature(new TemperatureImpl(40f));
        dummyConfigProviderIn.setEmptyTemperature(new TemperatureImpl(25f));

        fillLevel.update();

        assertThat(boilerModel.getFillLevel(), equalTo(100F));
    }

    @Test
    public void ifTemperatureIsBetweenMinAndTargetInterpolateFillLevel() {
        dummyConfigProviderIn.setTargetTemperature(new TemperatureImpl(40f));
        dummyConfigProviderIn.setEmptyTemperature(new TemperatureImpl(15f));

        dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(38f));
        fillLevel.update();
        assertThat(boilerModel.getFillLevel(), equalTo(74F));

        dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(32f));
        fillLevel.update();
        assertThat(boilerModel.getFillLevel(), equalTo(57F));

        dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(23f));
        fillLevel.update();
        assertThat(boilerModel.getFillLevel(), equalTo(37F));

        dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(18f));
        fillLevel.update();
        assertThat(boilerModel.getFillLevel(), equalTo(22F));
    }

    @Test
    public void ifTemperatureIsOverTargetInterpolateFillLevelAndHadNewTargetTillMidnight() {
        dummyConfigProviderIn.setTargetTemperature(new TemperatureImpl(40f));
        dummyConfigProviderIn.setEmptyTemperature(new TemperatureImpl(15f));

        // Neues maximum
        dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(51.37f));
        fillLevel.update();
        assertThat(boilerModel.getFillLevel(), equalTo(100F));

        dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(51.36999f));
        fillLevel.update();
        assertThat(boilerModel.getFillLevel(), equalTo(100F));

        dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(32f));
        fillLevel.update();
        assertThat(boilerModel.getFillLevel(), equalTo(47F));

        dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(23f));
        fillLevel.update();
        assertThat(boilerModel.getFillLevel(), equalTo(31F));

        dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(18f));
        fillLevel.update();
        assertThat(boilerModel.getFillLevel(), equalTo(18F));

        // Um Mitternacht wird maximum Resettet
        dummyTimeProvider.setTime(LocalDateTime.of(2015, 1, 1, 0, 10));

        dummyTemperatureAnalyzer.setTemperature(new TemperatureImpl(32f));
        fillLevel.update();
        assertThat(boilerModel.getFillLevel(), equalTo(57F));
    }
}
