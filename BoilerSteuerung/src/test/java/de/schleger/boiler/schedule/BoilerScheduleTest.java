package de.schleger.boiler.schedule;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.schleger.boiler.filter.DummyTemperatureActionFilter;
import de.schleger.boiler.filter.TemperatureActionFilter;
import de.schleger.boiler.schedule.BoilerScheduleImpl;

public class BoilerScheduleTest 
{
	private BoilerScheduleImpl boilerLogicImpl;
	private List<TemperatureActionFilter> actionFilterList;
	
	@Before
	public void setUp()
	{
		actionFilterList = new ArrayList<TemperatureActionFilter>();
		boilerLogicImpl = new BoilerScheduleImpl(actionFilterList);
	}
	
	@Test
	public void pruefeAlleActionFilter()
	{
		actionFilterList.add(new DummyTemperatureActionFilter());
		actionFilterList.add(new DummyTemperatureActionFilter());
		
		boilerLogicImpl.analyse();
		
		assertThat(((DummyTemperatureActionFilter)(actionFilterList.get(0))).isFiltered(), equalTo(true));
		assertThat(((DummyTemperatureActionFilter)(actionFilterList.get(1))).isFiltered(), equalTo(true));
	}

	
	@Test
	public void merktSichErstenActionFilterSoLangeBisNichtMehrAktiv()
	{
		DummyTemperatureActionFilter dummyTemperatureActionFilter = new DummyTemperatureActionFilter();		
		actionFilterList.add(dummyTemperatureActionFilter);
		actionFilterList.add(new DummyTemperatureActionFilter());
		
		dummyTemperatureActionFilter.setFilterResponse(true);
		boilerLogicImpl.analyse();
		assertThat(((DummyTemperatureActionFilter)(actionFilterList.get(0))).isFiltered(), equalTo(true));
		assertThat(((DummyTemperatureActionFilter)(actionFilterList.get(1))).isFiltered(), equalTo(false));
		
		dummyTemperatureActionFilter.setFilterResponse(false);
		dummyTemperatureActionFilter.setFiltered(false);
		boilerLogicImpl.analyse();		
		assertThat(((DummyTemperatureActionFilter)(actionFilterList.get(0))).isFiltered(), equalTo(true));
		assertThat(((DummyTemperatureActionFilter)(actionFilterList.get(1))).isFiltered(), equalTo(false));
		
		dummyTemperatureActionFilter.setFiltered(false);
		boilerLogicImpl.analyse();		
		assertThat(((DummyTemperatureActionFilter)(actionFilterList.get(0))).isFiltered(), equalTo(true));
		assertThat(((DummyTemperatureActionFilter)(actionFilterList.get(0))).isFiltered(), equalTo(true));
	}
}
