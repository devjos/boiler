package de.schleger.boiler.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import de.schleger.boiler.config.HeatPower;
import de.schleger.boiler.temperature.Temperature;

public class BoilerModel {

    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private float fillLevel = 0;
    private Temperature temperature = null;
    private HeatPower heatPower = null;

    public void setFillLevel(float fillLevel) {
        float old = this.fillLevel;
        this.fillLevel = fillLevel;
        pcs.firePropertyChange("fillLevel", old, this.fillLevel);
    }

    public void setTemperature(Temperature temp) {
        Temperature old = this.temperature;
        this.temperature = temp;
        pcs.firePropertyChange("temperature", old, this.temperature);
    }

    public void setHeatPower(HeatPower power) {
        HeatPower old = this.heatPower;
        this.heatPower = power;
        pcs.firePropertyChange("heatPower", old, this.heatPower);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(propertyName, listener);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public HeatPower getHeatPower() {
        return this.heatPower;
    }

    public Temperature getTemperature() {
        return this.temperature;
    }

    public float getFillLevel() {
        return this.fillLevel;
    }

}
