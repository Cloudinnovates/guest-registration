package com.exadel.guestregistration.services;

import org.mockito.Mockito;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import com.exadel.guestregistration.repositories.AgentRepository;
import com.exadel.guestregistration.repositories.GuestRepository;
import com.exadel.guestregistration.services.impl.AgentServiceImpl;
import com.exadel.guestregistration.services.impl.GuestServiceImpl;     


public class GuestServiceMockTestConfiguration {
	
    @MockBean
    private GuestRepository guestRepository;
    
    @MockBean
    private AgentRepository agentRepository;


    @Bean
    public GuestService guestService() { 
        return new GuestServiceImpl(); 
    }
    
    @Bean
    public AgentService agentService() { 
        return new AgentServiceImpl();     
    }

}   
