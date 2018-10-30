package com.exadel.guestregistration.services;
 
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder; 
import static org.junit.Assert.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;   
import com.exadel.guestregistration.models.Event;  
import com.exadel.guestregistration.repositories.EventRepository;
import com.exadel.guestregistration.services.impl.EventServiceImpl; 

public class EventServiceTestMockito {  
	
	@Mock
	private EventRepository eventRepositoryMock;  

	@InjectMocks    
	private EventServiceImpl eventServiceImpl;       
	
	@Captor
    private ArgumentCaptor<Event> customerArgument;    

	@Before
	public void setUp() throws Exception {      
		MockitoAnnotations.initMocks(this);   
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetEvents() throws Exception {   
		
		Event testEvent = createTestEvent(); 
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0); 

		Date todaysDate = cal.getTime();
		
		List<Event> allEvents = Arrays.asList(testEvent, testEvent, testEvent);       
   
		when(eventRepositoryMock.findEventsByDateFrom(todaysDate)).thenReturn(allEvents);         
		   
	    List<Event> receivedEvents = eventServiceImpl.getEvents();         
		
	    assertThat(receivedEvents, is(allEvents));         
		assertEquals(3, receivedEvents.size()); 
		
		assertThat(receivedEvents, containsInAnyOrder(
                hasProperty("location", is("TestLocation")),   
                hasProperty("location", is("TestLocation")), 
                hasProperty("location", is("TestLocation"))         
        ));	
		
		verify(eventRepositoryMock, times(1)).findEventsByDateFrom(todaysDate);      	  
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetPastEvents() throws Exception {     
		
		Event testEvent = createTestEvent(); 
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0); 

		Date todaysDate = cal.getTime();
		
		List<Event> allEvents = Arrays.asList(testEvent, testEvent, testEvent);       
   
		when(eventRepositoryMock.findEventsByDateTo(todaysDate)).thenReturn(allEvents);         
		   
	    List<Event> receivedEvents = eventServiceImpl.getPastEvents();           
		
	    assertThat(receivedEvents, is(allEvents));         
		assertEquals(3, receivedEvents.size()); 
		
		assertThat(receivedEvents, containsInAnyOrder(
                hasProperty("eventName", is("TestEvent")),   
                hasProperty("eventName", is("TestEvent")), 
                hasProperty("eventName", is("TestEvent"))         
        ));	
		
		verify(eventRepositoryMock, times(1)).findEventsByDateTo(todaysDate);             	  
	}
	
	
	@Test
	public void testGetSearchedEvents() throws Exception {
		
		Event testEvent = createTestEvent();
		
		List<Event> allEvents = Arrays.asList(testEvent, testEvent);  
		
		when(eventServiceImpl.getSearchedEvents("TestManager")).thenReturn(allEvents);  
		
		assertEquals(2, eventServiceImpl.getSearchedEvents("TestManager").size());  
		
		List<Event> tempList = eventServiceImpl.getSearchedEvents("TestManager");
		
		assertEquals("TestEvent", tempList.get(0).getEventName());   
		
		when(eventServiceImpl.getSearchedEvents("WrongEventName")).thenReturn(null);

        assertEquals(eventServiceImpl.getSearchedEvents("WrongEventName"), null);      
        
	} 
	
	
	@Test
	public void testCreateEvent() {   
		
		Event newEvent = createTestEvent(); 

	    eventServiceImpl.createEvent(newEvent);   

		verify(eventRepositoryMock).save(customerArgument.capture());

		assertThat(customerArgument.getValue().getEventName(), is(notNullValue()));
		assertThat(customerArgument.getValue(), hasProperty("eventName", is("TestEvent")));      
		
		assertThat(newEvent, hasProperty("eventName", is("TestEvent")));  
		
		when(eventRepositoryMock.save(any(Event.class))).thenReturn(new Event());   

		Event event = new Event();

		assertThat(eventServiceImpl.createEvent(event), is(notNullValue()));            
	}  
	
	
	@Test
    public void testGetEvent() { 
    	
    	Event testEvent = createTestEvent();
    	
        eventServiceImpl.createEvent(testEvent);   
        
        Mockito.when(eventRepositoryMock.findById(null)).thenReturn(Optional.of(createTestEvent()));   
        
        Event eventForComparison = eventServiceImpl.getEvent(testEvent.getId());   
        
        assertThat(testEvent, is(eventForComparison));  
        
        String eventName = eventForComparison.getEventName(); 
        
        assertEquals("TestEvent", eventName);         
    	
    }
		
	
	@Test
	public void testUpdateEvent() {   
		
		when(eventRepositoryMock.save(any(Event.class))).thenReturn(new Event());   
		
		Event event = new Event();
		assertThat(eventServiceImpl.updateEvent(event), is(new Event()));   

		verify(eventRepositoryMock).save(any(Event.class));      

		verify(eventRepositoryMock, times(1)).save(any(Event.class));       
	}
	
	
	@Test
	public void testDeleteEvent() {     
		
		when(eventRepositoryMock.save(any(Event.class))).thenReturn(new Event());            

		verify(eventRepositoryMock, never()).delete(any(Event.class));   
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

		return new Event("TestEvent", "TestManager", "TestLocation", testDateFrom, testDateTo, 20, "TestType", true);  
	} 
	
}  
