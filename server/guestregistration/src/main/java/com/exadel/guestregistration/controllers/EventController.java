package com.exadel.guestregistration.controllers; 

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.exadel.guestregistration.models.Event;
import com.exadel.guestregistration.services.EventService; 

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="http://localhost:4200", allowedHeaders="*")
public class EventController {
	
	@Autowired
	private EventService eventService;   
	

	@GetMapping("/events")
	public List<Event> getEvents() {
		return eventService.getEvents();
	}
	
	
	@GetMapping("/past-events")
	public List<Event> getPastEvents() {
		return eventService.getPastEvents();     
	}
	
	
	@GetMapping("/searched-events/{searchingEvent}")
	public List<Event> getSearchedEvents(@PathVariable String searchingEvent) {                                                       
		return eventService.getSearchedEvents(searchingEvent);                   
	}
    	

	@GetMapping("/event/{id}")
	public Event getEvent(@PathVariable String id) { 
		return eventService.getEvent(id); 
	}

	@DeleteMapping("/event/{id}")
	public boolean deleteEvent(@PathVariable String id) { 
		eventService.deleteEvent(id); 
		return true;
	}

	@PutMapping("/event")
	public Event updateEvent(@RequestBody Event event) {   
		return eventService.createEvent(event);     
	}

	@PostMapping("/event")                                            
	public Event createEvent(@RequestBody Event event) {
		System.err.println(event.isPrivate());
		return eventService.createEvent(event);    
	}
	
	@GetMapping("/events/public")
	public List<Event> getPublicEvents(){
		return eventService.getPublicEvents();
	}

	@GetMapping("/events/current_public")
	public List<Event> getCurrentPublicEvents(){
		return eventService.getPublicCurrentEvents();
	}

	@GetMapping("/events/current")
	public List<Event> getCurrentEvents(){
		return eventService.getCurrentEvents();
	}


} 


 

