package de.schleger.boiler.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import de.schleger.boiler.config.HeatPower;
import de.schleger.boiler.temperature.TemperatureImpl;

public class BoilerModelTest {

    private BoilerModel model;

    @Before
    public void setUp() {
        model = new BoilerModel();
    }

    @Test
    public void firesFillLevelChanges() {
        MyPropertyChangeListener pcl = new MyPropertyChangeListener();
        model.addPropertyChangeListener("fillLevel", pcl);
        model.setFillLevel(123f);
        assertThat(pcl.wasExecuted(), equalTo(true));
    }

    @Test
    public void firesTemperatureChanges() {
        MyPropertyChangeListener pcl = new MyPropertyChangeListener();
        model.addPropertyChangeListener("temperature", pcl);
        model.setTemperature(new TemperatureImpl(345f));
        assertThat(pcl.wasExecuted(), equalTo(true));
        assertThat(pcl.getEvent().getNewValue(), equalTo(new TemperatureImpl(345f)));
    }

    @Test
    public void firesHeatPowerChanges() {
        MyPropertyChangeListener pcl = new MyPropertyChangeListener();
        model.addPropertyChangeListener("heatPower", pcl);
        model.setHeatPower(HeatPower.HEAT_POWER_1);
        assertThat(pcl.wasExecuted(), equalTo(true));
        assertThat(pcl.getEvent().getNewValue(), equalTo(HeatPower.HEAT_POWER_1));
    }

    @Test
    public void doesntFireOtherChanges() {
        MyPropertyChangeListener pcl = new MyPropertyChangeListener();
        model.addPropertyChangeListener("fejsiafjlasfeasf", pcl);
        model.setHeatPower(HeatPower.HEAT_POWER_1);
        model.setFillLevel(2345f);
        model.setTemperature(new TemperatureImpl(34243f));
        assertThat(pcl.wasExecuted(), equalTo(false));
    }

}
