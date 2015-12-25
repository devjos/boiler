package de.schleger.boiler.filter;

public class DummyTemperatureActionFilter implements TemperatureActionFilter
{
	private boolean isFiltered;
	private boolean filterResponse;
	
	
	@Override
	public boolean filter() 
	{
		setFiltered(true);
		
		return isFilterResponse();
	}

	public boolean isFiltered() 
	{
		return isFiltered;
	}
	
	public void setFiltered(boolean isFiltered) 
	{
		this.isFiltered = isFiltered;
	}
	
	public boolean isFilterResponse() 
	{
		return filterResponse;
	}
	
	public void setFilterResponse(boolean filterResponse) 
	{
		this.filterResponse = filterResponse;
	}
}
