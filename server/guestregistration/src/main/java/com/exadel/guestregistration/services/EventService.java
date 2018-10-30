package com.exadel.guestregistration.services;

import java.util.List;

import com.exadel.guestregistration.models.Event;


public interface EventService {  
	
	public List<Event> getEvents(); 
	
	public List<Event> getPastEvents();     
	
	public List<Event> getSearchedEvents(String searchingEvent);     
	
	public Event createEvent(Event theEvent);       
	
	public Event getEvent(String theId);
	
	public boolean deleteEvent(String theId);  
	
	public Event updateEvent(Event theEvent);

	List<Event> getPublicEvents();

	List<Event> getPublicCurrentEvents();

	List<Event> getCurrentEvents();

}   


