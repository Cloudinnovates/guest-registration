package com.exadel.guestregistration.controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exadel.guestregistration.exceptions.NotFoundException;
import com.exadel.guestregistration.models.Agent;
import com.exadel.guestregistration.models.Card;
import com.exadel.guestregistration.models.CardInfo;
import com.exadel.guestregistration.models.Employee;
import com.exadel.guestregistration.models.Guest;
import com.exadel.guestregistration.models.Office;
import com.exadel.guestregistration.repositories.GuestRepository;
import com.exadel.guestregistration.services.AgentService;
import com.exadel.guestregistration.services.CardInfoService;
import com.exadel.guestregistration.services.CardService;
import com.exadel.guestregistration.services.EmployeeService;
import com.exadel.guestregistration.services.GuestService;
import com.exadel.guestregistration.services.OfficeService;
 

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="http://localhost:4200", allowedHeaders="*")
public class CardInfoController {   
	
	@Autowired
	private AgentService agentService; 
	
	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private CardService cardService;   
	
	@Autowired
	private GuestRepository guestRepository;  
		
	
	@GetMapping("/card-history/{searchingCardId}")
	public List<CardInfo> getSearchedCards(@PathVariable String searchingCardId) throws NotFoundException {  
		
		if (!guestRepository.findGuestsByField(searchingCardId).isEmpty()) {  
			
			List<CardInfo> cardInfoList = new ArrayList<CardInfo>();
		
			List<Guest> tempGuest = guestRepository.findGuestsByField(searchingCardId); 
			
			for (Guest guest: tempGuest) {
			
			DateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");    
			
			Date arrivalDate = null;
			try {
				arrivalDate = sourceFormat.parse(guest.getArrived());     
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			Date leavingDate = null;
			try {
				leavingDate = sourceFormat.parse(guest.getLeftForDisplay());           
			} catch (NullPointerException e) {
				System.out.println("Leaving date was not set yet");  
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			Agent agentGuest =  agentService.findAgentById(guest.getGuestId());   
			 
			Card cardGuest = cardService.getCard(guest.getCardId());   
			
			Office officeGuest = officeService.findOfficeById(cardGuest.getOfficeId());     
			
			CardInfo cardInfoForGuest = new CardInfo(guest.getId(), cardGuest.getCardNo(),
					                         agentGuest.getName(), agentGuest.getSurname(),
					                         cardGuest.getCardType(), officeGuest.getName() + " Office",
					                         arrivalDate, leavingDate);       
	
			
			cardInfoList.add(cardInfoForGuest);   
			
			}
			                     
			return cardInfoList;   
		
		} 
		
		
		else {
			
			if (!employeeService.findEmployeesByCardId(searchingCardId).isEmpty()) {
				
				List<CardInfo> cardInfoList = new ArrayList<CardInfo>();
				
				List<Employee> tempEmployee = employeeService.findEmployeesByCardId(searchingCardId); 
				
				for (Employee employee: tempEmployee) {
				
				Agent agentEmployee = agentService.findAgentById(employee.getAgentId());       
				
		        Card cardEmployee = cardService.getCard(employee.getCardId());   
				
				Office officeEmployee = officeService.findOfficeById(cardEmployee.getOfficeId());  
				
				
				CardInfo cardInfoForEmployee = new CardInfo(employee.getId(), cardEmployee.getCardNo(),       
						agentEmployee.getName(), agentEmployee.getSurname(), cardEmployee.getCardType(),
						officeEmployee.getName() + " Office", employee.getCard_issue_date(), employee.getDismissal_date());    
		
				
				cardInfoList.add(cardInfoForEmployee);   
				
				}
				                     
				return cardInfoList;
			}
    
			else {
				
				return null;   
			}
		
		}  
	}
	
	
	
	
	


}  
