package de.schleger.boiler.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MyPropertyChangeListener implements PropertyChangeListener{
	
	private boolean wasExecuted = false;
	private PropertyChangeEvent event;

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		wasExecuted = true;
		event = evt;
	}
	
	public boolean wasExecuted(){
		return wasExecuted;
	}
	
	public PropertyChangeEvent getEvent(){
		return event;
	}

}
