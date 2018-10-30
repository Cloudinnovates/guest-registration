package com.exadel.guestregistration.services;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import com.exadel.guestregistration.repositories.AgentRepository;
import com.exadel.guestregistration.repositories.CardRepository;
import com.exadel.guestregistration.repositories.GuestRepository;
import com.exadel.guestregistration.repositories.OfficeRepository;
import com.exadel.guestregistration.services.impl.AgentServiceImpl;
import com.exadel.guestregistration.services.impl.CardServiceImpl;
import com.exadel.guestregistration.services.impl.GuestServiceImpl;
import com.exadel.guestregistration.services.impl.OfficeServiceImpl;     

public class CardInfoControllerMockTestConfiguration {
	
	   @MockBean
	    private GuestRepository guestRepository;
	    
	    @MockBean
	    private AgentRepository agentRepository;
	    
	    @MockBean
	    private CardRepository cardRepository; 
	    
	    @MockBean
	    private OfficeRepository officeRepository;  


	    @Bean
	    public GuestService guestService() { 
	        return new GuestServiceImpl(); 
	    }
	    
	    @Bean
	    public AgentService agentService() { 
	        return new AgentServiceImpl();     
	    } 
	    
	    @Bean
	    public CardService cardService() { 
	        return new CardServiceImpl();        
	    } 
	    
	    @Bean
	    public OfficeService officeService() { 
	        return new OfficeServiceImpl();        
	    }
	    

}
