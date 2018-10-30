package com.exadel.guestregistration.controllers; 

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.exadel.guestregistration.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exadel.guestregistration.exceptions.NotFoundException;
import com.exadel.guestregistration.models.Agent;
import com.exadel.guestregistration.models.Card;
import com.exadel.guestregistration.models.Guest;
import com.exadel.guestregistration.models.Office;
import com.exadel.guestregistration.services.AgentService;
import com.exadel.guestregistration.services.CardService;
import com.exadel.guestregistration.services.GuestService;
import com.exadel.guestregistration.services.OfficeService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="http://localhost:4200", allowedHeaders="*")
public class GuestController {
	
	@Autowired
	private GuestService guestService;  
	
	@Autowired
	private CardService cardService;
	
	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private AgentService agentService;  
		
	
	
	@GetMapping("/guests")  
	public List<Guest> getGuests() throws NotFoundException {    
		
		List<Guest> guestList = new ArrayList<Guest>();  
		
		List<Guest> allGuests = guestService.getGuests();
		
		for (Guest tempGuest: allGuests) {
			
			DateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");  
	    	//sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			
			Date arrivalDate = null;
			try {
				arrivalDate = sourceFormat.parse(tempGuest.getArrived());    
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			Date leavingDate = null;
			try {
				leavingDate = sourceFormat.parse(tempGuest.getLeftForDisplay());           
			} catch (NullPointerException e) {
				System.out.println("Leaving date was not set yet");  
			} catch (ParseException e) {
				e.printStackTrace();
			}  
				
			
			Card oneCard = cardService.getCard(tempGuest.getCardId());  
			
			Office oneOffice = officeService.findOfficeById(oneCard.getOfficeId()); 
			
			Agent newAgent = agentService.findAgentById(tempGuest.getGuestId());    
			
			Guest completeGuest = new Guest( tempGuest.getId(), newAgent.getName(), newAgent.getSurname(),    
                    						 tempGuest.getVisitType(), arrivalDate, leavingDate, 
                    						 oneCard.getCardNo(), oneOffice.getName(), oneOffice.getManager_name(), 
                    						 tempGuest.getComment(), tempGuest.getCardId(), tempGuest.getGuestId());                   
			
			guestList.add(completeGuest); 
			
		}
		
		return guestList;        
	}  
	
	
	@GetMapping("/searched-guests/{searchingGuest}")   
	public List<Guest> getSearchedGuests(@PathVariable String searchingGuest) {                                                       
		return guestService.getSearchedGuests(searchingGuest);    
	}
	
	@GetMapping("/searched-participants/{searchingParticipant}")   
	public List<Agent> getSearchedParticipants(@PathVariable String searchingParticipant) {                                                         
		return guestService.getSearchedParticipants(searchingParticipant);                        
	} 
    	

	@GetMapping("/guest/{id}")
	public Guest getGuest(@PathVariable String id) {      
		return guestService.getGuest(id); 
	}

	@DeleteMapping("/guest/{id}")
	public boolean deleteGuest(@PathVariable String id) { 
		guestService.deleteGuest(id); 
		return true;
	}

	@PutMapping("/guest")
	public Guest updateGuest(@RequestBody Guest guest) {     
		return guestService.createGuest(guest);      
	}  
	
	@PostMapping("/guest")                                            
	public Guest createGuest(@RequestBody Guest guest) throws ParseException {   
		return guestService.createGuest(guest);   
	} 
	
	@GetMapping("/sorted-cards")
    public List<Card> getSortedCards() {   
		
		List<Card> myCards = cardService.getCards();  
		
		List<Card> sortedCards = new ArrayList<Card>();
		
		for (Card tempCard: myCards) {
			if (tempCard.isCardAvailable() && tempCard.getCardType().equals("Guest")) {          
				sortedCards.add(tempCard);
			}
		}
        return sortedCards;        
    } 
	

} 

 

