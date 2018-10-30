package com.exadel.guestregistration.services;

import static org.hamcrest.CoreMatchers.equalTo; 
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.exadel.guestregistration.models.Event;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@SpringBootTest
public class EventServiceTest {

	@Autowired
	private EventService eventService;
	
	@Before
	public void setup() {
		Assert.assertNotNull(eventService);  
	}


	@Test
	public void testGetEvents() {
		
		Event testEvent = createTestEvent();
		eventService.createEvent(testEvent); 

		List<Event> actual = eventService.getEvents();
		int actualSize = actual.size();
		assertThat(actual.size(), is(actualSize));

		actual = eventService.getEvents();
		List<Event> expected = eventService.getEvents();  
		assertThat(actual, is(expected));

		Event eventForChecking =
				     eventService.getEvent(testEvent.getId()); 
		assertThat(actual, hasItems(eventForChecking));
		
		eventService.deleteEvent(testEvent.getId());    

	}


	@Test
	public void testGetPastEvents() {
		
		Event testEvent = createTestPastEvent();  
		eventService.createEvent(testEvent); 
		eventService.createEvent(testEvent);  
		
		List<Event> events = eventService.getPastEvents();
		assertThat(events, not(IsEmptyCollection.empty()));

		int number = events.size();
		assertThat(events, hasSize(number));

		Event eventForChecking = eventService.getEvent(testEvent.getId()); 
		assertThat(events, hasItems(eventForChecking));
		
		eventService.deleteEvent(testEvent.getId());
		eventService.deleteEvent(testEvent.getId());

	}


	@SuppressWarnings("unchecked")
	@Test
	public void testGetSearchedEvents() {
		
		Event testEvent = createTestEvent();
		eventService.createEvent(testEvent);
		eventService.createEvent(testEvent);
		eventService.createEvent(testEvent);
		
		List<Event> actual = eventService.getSearchedEvents("TestEvent");
		List<Event> expected = eventService.getSearchedEvents("TestEvent");  

		assertThat(actual, is(expected));

		assertThat(actual, containsInAnyOrder(
				hasProperty("manager", is("TestManager")),
				hasProperty("manager", is("TestManager")),
				hasProperty("manager", is("TestManager"))
		));

		assertThat(actual, containsInAnyOrder(
				hasProperty("location", is("TestLocation")),
				hasProperty("location", is("TestLocation")),
				hasProperty("location", is("TestLocation")) 
		));
		
		eventService.deleteEvent(testEvent.getId());
		eventService.deleteEvent(testEvent.getId());
		eventService.deleteEvent(testEvent.getId());     
	}


	@Test
	public void testCreateEvent() {
		Event testEvent = createTestEvent();
		eventService.createEvent(testEvent);

		Event eventForComparison = eventService.getEvent(testEvent.getId());
		assertEquals("should be equals", testEvent, eventForComparison);  
	}


	@Test
	public void testGetEvent() {
		Event testEvent = createTestEvent();
		eventService.createEvent(testEvent);

		Event eventForComparison = eventService.getEvent(testEvent.getId());
		assertNotNull("eventForComparison should not be null", eventForComparison);  

		assertThat(eventForComparison, hasProperty("eventName", is("TestEvent"))); 
	}


	@Test
	public void testDeleteEvent() {
		Event testEvent = createTestEvent();
		eventService.createEvent(testEvent);

		List<Event> eventsBeforeDelete = eventService.getEvents();
		int sizeBeforeDelete = eventsBeforeDelete.size();
		assertEquals(eventsBeforeDelete.size(), sizeBeforeDelete);

		eventService.deleteEvent(testEvent.getId());

		List<Event> eventsAfterDelete = eventService.getEvents();
		int sizeAfterDelete = eventsAfterDelete.size();
		assertEquals(eventsBeforeDelete.size()-1, sizeAfterDelete);
	}


	@Test
	public void testUpdateEvent() {
		Event testEvent = createTestEvent();
		eventService.createEvent(testEvent);

		Event eventBeforeUpdate = eventService.getEvent(testEvent.getId());
		Event eventForComparison = eventService.getEvent(testEvent.getId());

		eventBeforeUpdate.setEventName("New Event Name");
		eventService.updateEvent(eventBeforeUpdate);

		Event updatedEvent = eventService.getEvent(eventBeforeUpdate.getId());

		assertThat(eventForComparison, is(not(updatedEvent)));
		assertThat(eventForComparison, not(equalTo(updatedEvent))); 
		
		eventService.deleteEvent(testEvent.getId()); 
	}


	public Event createTestEvent() {

		DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy");   
		String dateFromAsString = "01-12-2018";
		Date testDateFrom = null;
		try {
			testDateFrom = sourceFormat.parse(dateFromAsString);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		String dateToAsString = "03-12-2018";
		Date testDateTo = null;
		try {
			testDateTo = sourceFormat.parse(dateToAsString);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return new Event("TestEvent", "TestManager", "TestLocation", testDateFrom, testDateTo, 20, "TestType", false);
	}
	
	public Event createTestPastEvent() {

		DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy");
		String dateFromAsString = "01-10-2018";
		Date testDateFrom = null;
		try {
			testDateFrom = sourceFormat.parse(dateFromAsString);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		String dateToAsString = "03-10-2018";
		Date testDateTo = null;
		try {
			testDateTo = sourceFormat.parse(dateToAsString);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return new Event("PastTestEvent", "PastTestManager", "PastTestLocation",
				          testDateFrom, testDateTo, 20, "PastTestType", true);
	}

}   
