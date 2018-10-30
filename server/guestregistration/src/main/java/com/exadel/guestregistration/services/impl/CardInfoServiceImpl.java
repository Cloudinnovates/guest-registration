package com.exadel.guestregistration.services.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; 
import com.exadel.guestregistration.models.CardInfo;
import com.exadel.guestregistration.models.Guest;
import com.exadel.guestregistration.repositories.CardInfoRepository;  
import com.exadel.guestregistration.repositories.GuestRepository;
import com.exadel.guestregistration.services.CardInfoService;

@Service
public class CardInfoServiceImpl implements CardInfoService {    
	
	@Autowired	
	private CardInfoRepository cardInfoRepository; 	
	
	@Autowired
	private GuestRepository guestRepository; 
	
	
	@Override
	@Transactional
	public List<Guest> getSearchedGuests(String searchingGuestId) {          
		
		List<Guest> searchedList = guestRepository.findGuestsByField(searchingGuestId);        
		
		return searchedList;
		 
	}
	
	

}  
