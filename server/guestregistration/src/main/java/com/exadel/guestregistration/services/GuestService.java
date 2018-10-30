package com.exadel.guestregistration.services;

import java.util.List;

import com.exadel.guestregistration.models.Agent;
import com.exadel.guestregistration.models.Guest;

public interface GuestService {
	
	public List<Guest> getGuests();     
	
	public List<Guest> getSearchedGuests(String searchingGuest);       
	
	public List<Agent> getSearchedParticipants(String searchingParticipant);          
	
	public Guest createGuest(Guest theGuest);        
	
	public Guest getGuest(String theId);
	
	public boolean deleteGuest(String theId);  
	
	public Guest updateGuest(Guest theGuest);               

}   



