package com.exadel.guestregistration.services.impl;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import com.exadel.guestregistration.services.EventService; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; 
import com.exadel.guestregistration.models.Event;
import com.exadel.guestregistration.repositories.EventRepository;


@Service
public class EventServiceImpl implements EventService {
	
	private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);    
	
	@Autowired	
	private EventRepository eventRepository;	
	
		
	@Override
	@Transactional
	public List<Event> getEvents() {                 
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0); 

		Date todaysDate = cal.getTime();
		
		List<Event> sortedList = eventRepository.findEventsByDateFrom(todaysDate);      
		
		logger.debug("Upcoming events are: " + sortedList);
		
		return sortedList;      
	}
	
	
	@Override
	@Transactional
	public List<Event> getPastEvents() {     
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0); 

		Date todaysDate = cal.getTime();
		
		List<Event> sortedList = eventRepository.findEventsByDateTo(todaysDate);   
		
		logger.debug("Past events are: " + sortedList); 
		
		return sortedList;     
	}
		
	
	@Override
	@Transactional
	public List<Event> getSearchedEvents(String searchingEvent) {        
		
		logger.debug("User is searching for: " + searchingEvent);         
		
		if (searchingEvent.contains("-") || 
			searchingEvent.contains(".") ||
			searchingEvent.contains("/") ||
			searchingEvent.contains(" ")) {    
				
				List<Event> searchedList = null;
				try {
					searchedList = eventRepository.findEventsByStartDate(isValidDate(searchingEvent)); 
				} catch (ParseException e) {
					System.out.println("Cannot be parsed to Date");
				}  
				
					if (!searchedList.isEmpty()) {
						return searchedList;
					} 
					
					else {
						try {
							searchedList = eventRepository.findEventsByEndDate(isValidDate(searchingEvent));    
						} catch (ParseException e) {
							System.out.println("Cannot be parsed to Date");
						}
						return searchedList;  
					}		    
			
		}
		
		if (isStringInt(searchingEvent)) {     
			
			int number = Integer.parseInt(searchingEvent);   
			   
			List<Event> searchedList = eventRepository.findEventsByParticipantNumber(number);                              
			
			return searchedList;
			               
		} 
		
		else  { 
			
			List<Event> searchedList = eventRepository.findEventsByField(searchingEvent);   
		
			return searchedList;  
		
		}
		 
	}  
	
	
	
	@Override	
	@Transactional
	public Event createEvent(Event theEvent) {	
		
		if (theEvent.getId() != null) { 
			
			Event eventBeforeUpdate = eventRepository.findById(theEvent.getId()).get();        
			
			logger.debug("Event before update: " + "Event Name- " + eventBeforeUpdate.getEventName() + ", "    
                                                 + "Manager- " + eventBeforeUpdate.getManager() + ", "
                                                 + "Location- " + eventBeforeUpdate.getLocation());       
			
			logger.debug("Event after update: " + "Event Name- " + theEvent.getEventName() + ", "    
                                                + "Manager- " + theEvent.getManager() + ", "
                                                + "Location- " + theEvent.getLocation());  
			
			return eventRepository.save(theEvent);
			
		} 
		
		else {
		
			logger.debug("New Event created: " + "Event Name- " + theEvent.getEventName() + ", "   
			                                   + "Manager- " + theEvent.getManager() + ", "
			                                   + "Location- " + theEvent.getLocation());      
			
			return eventRepository.save(theEvent); 
		}
	}	    

	@Override
	@Transactional
	public Event getEvent(String theId) {			
		return eventRepository.findById(theId).get();       
	}	

	@Override	
	@Transactional	
	public boolean deleteEvent(String theId) {		 		
		eventRepository.deleteById(theId);   
		return true;    	
	 }
	
	@Override	
	@Transactional	
	public Event updateEvent(Event theEvent) { 
		return eventRepository.save(theEvent);        
	 }
	
	
	public boolean isStringInt(String searchingEvent) {
	    try {
	        Integer.parseInt(searchingEvent);
	        return true;
	    } catch (NumberFormatException ex) { 
	        return false;
	    }
	}

	
	public static Date isValidDate(String searchingDate) throws ParseException { 
		
		if (searchingDate.contains("-")) { 
			
			DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");   
		    formatter.setTimeZone(TimeZone.getTimeZone("UTC")); 
		    Date myDate = null;
			try {
				myDate = formatter.parse(searchingDate);

			} catch (ParseException e) {
				System.out.println("Go on");   
			}
			
	        return myDate; 
		}
		
		else if (searchingDate.contains(".")) {

			DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");    
		    formatter.setTimeZone(TimeZone.getTimeZone("UTC")); 
		    Date myDate = null;
			try {
				myDate = formatter.parse(searchingDate); 
				
			} catch (ParseException e) {
				System.out.println("Go on");    
			}
			
		    return myDate;      
		}
		
		else if (searchingDate.contains("/")) {

			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");    
		    formatter.setTimeZone(TimeZone.getTimeZone("UTC")); 
		    Date myDate = null;
			try {
				myDate = formatter.parse(searchingDate); 
				
			} catch (ParseException e) {
				System.out.println("Go on");    
			}
			
		    return myDate;      
		}
		
		else if (searchingDate.contains(" ")) {

			DateFormat formatter = new SimpleDateFormat("dd MM yyyy");    
		    formatter.setTimeZone(TimeZone.getTimeZone("UTC")); 
		    Date myDate = null;
			try {
				myDate = formatter.parse(searchingDate); 
				
			} catch (ParseException e) {
				System.out.println("Go on");    
			}
			
		    return myDate;      
		}
		
		return null;    
	 	
   }

	@Override
	public List<Event> getPublicEvents() {
		return eventRepository.findPublicEvents();
	}

	@Override
	public List<Event> getPublicCurrentEvents() {
		return eventRepository.findPublicCurrentEvents(new Date());
	}

	@Override
	public List<Event> getCurrentEvents() {
		return eventRepository.findCurrentEvents(new Date());
	}
}













