package de.schleger.boiler.information;

public class DummyInformationProvider implements InformationProvider {
	
	private boolean isUpdated = false;

	@Override
	public void updateInformation() {
		isUpdated = true;
	}
	
	public boolean isUpdated(){
		return isUpdated;
	}


}
