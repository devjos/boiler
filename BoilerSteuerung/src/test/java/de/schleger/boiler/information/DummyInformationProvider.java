package de.schleger.boiler.information;

public class DummyInformationProvider implements InformationUpdater {
	
	private boolean isUpdated = false;

	@Override
	public void update() {
		isUpdated = true;
	}
	
	public boolean isUpdated(){
		return isUpdated;
	}
}
