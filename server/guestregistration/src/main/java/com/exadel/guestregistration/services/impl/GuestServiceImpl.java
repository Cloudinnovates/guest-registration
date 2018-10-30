package com.exadel.guestregistration.services.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exadel.guestregistration.models.Agent;
import com.exadel.guestregistration.models.Guest;
import com.exadel.guestregistration.repositories.AgentRepository;
import com.exadel.guestregistration.repositories.GuestRepository;
import com.exadel.guestregistration.services.GuestService;   


@Service
public class GuestServiceImpl implements GuestService { 
	
	private static final Logger logger = LoggerFactory.getLogger(GuestServiceImpl.class);     
	
	@Autowired	
	private GuestRepository guestRepository; 
	
	@Autowired
	private AgentRepository agentRepository;
	
		
	@Override
	@Transactional
	public List<Guest> getGuests() { 
		return guestRepository.findAll();   
	}
		
	
	@Override
	@Transactional
	public List<Guest> getSearchedGuests(String searchingGuest) {      
		
		logger.debug("User is searching for: " + searchingGuest);         
		
		if (searchingGuest.contains("-") || 
			searchingGuest.contains(".") ||
			searchingGuest.contains("/") ||
			searchingGuest.contains(" ")) {      
				
				List<Guest> searchedList = null;
				try {
					searchedList = guestRepository.findGuestsByStartDate(isValidDate(searchingGuest)); 
				} catch (ParseException e) {
					System.out.println("Cannot be parsed to Date");
				}  
				
					if (!searchedList.isEmpty()) {
						return searchedList;
					} 
					
					else {
						try {
							searchedList = guestRepository.findGuestsByEndDate(isValidDate(searchingGuest));   
						} catch (ParseException e) {
							System.out.println("Cannot be parsed to Date");
						}
						return searchedList;  
					}		    
			
		}
		
		if (isStringInt(searchingGuest)) {     
			
			int number = Integer.parseInt(searchingGuest);   
			   
			List<Guest> searchedList = guestRepository.findGuestsByParticipantNumber(number);                               
			
			return searchedList;
			               
		} 
		
		else  { 
			
			List<Guest> searchedList = guestRepository.findGuestsByField(searchingGuest);     
		
			return searchedList;  
		
		}
		 
	}  
	
	
	@Override
	@Transactional
	public List<Agent> getSearchedParticipants(String searchingParticipant) {   
		
		logger.debug("User is searching for: " + searchingParticipant);   
		
		List<Agent> searchedList = agentRepository.findAgentsByText(searchingParticipant);                
		
		return searchedList;
	}
	
	
	@Override	
	@Transactional
	public Guest createGuest(Guest theGuest) {	                                   
		
		DateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");   
    	//sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		Date arrivalDate = null;
		try {
			arrivalDate = sourceFormat.parse(theGuest.getArrived());    
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Date leavingDate = null;
		try {
			leavingDate = sourceFormat.parse(theGuest.getLeftForDisplay());  
		} catch (NullPointerException e) {
			System.out.println("Leaving date was not set");   
		} catch (ParseException e) {
			e.printStackTrace();
		}
		 
		
			logger.debug("New Event created: " + "First Name- " + theGuest.getFirstName() + ", "   
			                                   + "Last Name- " + theGuest.getLastName()); 
			
			
			Guest newGuest = new Guest(theGuest.getId(), theGuest.getVisitType(),
					                   arrivalDate, leavingDate,
	                                   theGuest.getComment(), theGuest.getCardId(), theGuest.getGuestId());         
			
			return guestRepository.save(newGuest);       
		
	}      
	

	@Override
	@Transactional
	public Guest getGuest(String theId) {			
		return guestRepository.findById(theId).get();       
	}	

	@Override	
	@Transactional	
	public boolean deleteGuest(String theId) {		 		
		guestRepository.deleteById(theId);   
		return true;    	
	}
	
	@Override	
	@Transactional	
	public Guest updateGuest(Guest theGuest) { 
		
		DateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");   
		
		Date arrivalDate = null;
		try {
			arrivalDate = sourceFormat.parse(theGuest.getArrived());    
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Date leavingDate = null;
		try {
			leavingDate = sourceFormat.parse(theGuest.getLeftForDisplay());  
		} catch (NullPointerException e) {
			System.out.println("Leaving date was not set");   
		} catch (ParseException e) {
			e.printStackTrace();
		}
		          
			
			Guest guestBeforeUpdate = guestRepository.findById(theGuest.getId()).get();        
			
			logger.debug("Guest before update: " + "First Name- " + guestBeforeUpdate.getFirstName() + ", "    
                                                 + "Last Name- " + guestBeforeUpdate.getLastName());          
			
			logger.debug("Guest after update: " + "Guest Name- " + theGuest.getFirstName() + ", "    
                                                + "Last Name- " + theGuest.getLastName());  
			
			Guest newGuest = new Guest(theGuest.getId(), theGuest.getVisitType(),
					                   arrivalDate, leavingDate,
					                   theGuest.getComment(), theGuest.getCardId(), theGuest.getGuestId());                     
			
			return guestRepository.save(newGuest); 
	            
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

	
}   














