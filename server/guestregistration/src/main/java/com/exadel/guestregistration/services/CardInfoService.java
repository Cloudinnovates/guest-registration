package com.exadel.guestregistration.services;

import java.util.List;

import com.exadel.guestregistration.models.CardInfo;
import com.exadel.guestregistration.models.Guest;

public interface CardInfoService { 
    
    public List<Guest> getSearchedGuests(String searchingGuestId);      
	

} 
