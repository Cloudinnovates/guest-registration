package com.exadel.guestregistration.services;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;  
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner; 
import com.exadel.guestregistration.models.Agent;
import com.exadel.guestregistration.models.Guest;
import com.exadel.guestregistration.repositories.AgentRepository;
import com.exadel.guestregistration.repositories.GuestRepository;
import com.exadel.guestregistration.services.impl.GuestServiceImpl; 


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = GuestServiceMockTestConfiguration.class)
public class GuestServiceMockTest {
	

    private static final String GUEST_ID = "ID-1";   
    
    @Autowired
    private GuestRepository guestRepository; 

    @Autowired
    private GuestService guestService;
    
    @Autowired
    private AgentRepository agentRepository;
    
    @Autowired
    private AgentService agentService; 
      

    @Before
    public void setup() {   
        Mockito.when(guestRepository.findById(GUEST_ID)).thenReturn(Optional.of(createTestGuest()));               
    }

    @After
    public void clean() {

    }

    @Test
    public void testLoadedServiceAndDAOs() {
        assertNotNull(guestRepository);
        assertNotNull(guestService);
        assertNotNull(agentRepository);

        assertTrue(guestService instanceof GuestServiceImpl);  

        assertTrue(guestRepository.getClass().getSimpleName().contains("$MockitoMock$"));   
        assertTrue(agentRepository.getClass().getSimpleName().contains("$MockitoMock$"));      

    }
    
    
    @Test   
	public void testGetGuests() throws Exception {   
    	
        Guest testGuest = createTestGuest(); 
        
        List<Guest> testGuestList = Arrays.asList(testGuest, testGuest, testGuest);                          
        when(guestRepository.findAll()).thenReturn(testGuestList);       
              
        List<Guest> guestList = guestService.getGuests();         
        
        assertNotNull("should not be null", guestList); 
        
        int listSize = guestList.size(); 
        
        String comparisonName = guestList.get(0).getFirstName();    
        
        assertThat(3, is(listSize)); 
        assertEquals("TestName", comparisonName);      
        
        String comparisonVisitType = guestList.get(1).getVisitType();  
        
        assertEquals("TestVisitType", comparisonVisitType);      
           
    } 
     
    
    @Test
	public void testGetSearchedGuests() throws Exception {
		
    	Guest testGuest = createTestGuest();
		
		List<Guest> allGuests = Arrays.asList(testGuest, testGuest);    
		
		when(guestService.getSearchedGuests("TestVisitType")).thenReturn(allGuests);   
		
		assertThat(allGuests, hasItems(testGuest, testGuest));    
		
		int allGuestsListSize = guestService.getSearchedGuests("TestVisitType").size();
		
		assertEquals(2, allGuestsListSize);     
		
		List<Guest> tempList = guestService.getSearchedGuests("TestVisitType");
		
		assertEquals("TestName", tempList.get(0).getFirstName());  
		
		when(guestService.getSearchedGuests("WrongGuestName")).thenReturn(null);   

        assertEquals(guestService.getSearchedGuests("WrongGuestName"), null);           
        
	}
    
    
    @Test
	public void testGetSearchedParticipants() throws Exception {
		
    	Agent testAgent = createTestAgent(); 
		
		List<Agent> allAgents = Arrays.asList(testAgent, testAgent);      
		
		when(agentRepository.findAgentsByText("TestAgentName")).thenReturn(allAgents);   
		
		assertEquals(2, agentRepository.findAgentsByText("TestAgentName").size());   
		
		List<Agent> tempList = agentRepository.findAgentsByText("TestAgentName");  
		
		assertEquals("TestAgentName", tempList.get(0).getName());      
		
		assertSame("should be same", tempList, tempList);  
		
		when(agentRepository.findAgentsByText("WrongAgentName")).thenReturn(null);   

        assertEquals(agentRepository.findAgentsByText("WrongAgentName"), null);              
        
	}


    @Test
    public void testCreateGuest() throws Exception {      

        Guest testGuest = createTestGuest(); 
        
        guestService.createGuest(testGuest);  
        
        Guest guest = guestService.getGuest(testGuest.getId());

        assertThat(guest, hasProperty("lastName"));

        assertThat(guest, hasProperty("lastName", is("TestLastName")));      

        Mockito.verify(guestRepository, times(1)).save(any());    
        
        when(guestRepository.save(any(Guest.class))).thenReturn(testGuest);

        assertThat(guestService.createGuest(testGuest), is(notNullValue()));   
      
    }
    
    
    @Test
    public void testGetGuest() {
    	
    	Guest testGuest = createTestGuest();
    	
        guestService.createGuest(testGuest); 
        
        Guest guestForComparison = guestService.getGuest(testGuest.getId());  
        
        assertThat(guestForComparison, hasProperty("comment"));

        assertThat(guestForComparison, hasProperty("comment", is("TestComment")));      
        
        assertNotNull("should not be null", guestForComparison);  
        
        assertSame("should be same", guestForComparison, guestForComparison);   
        
        assertThat(testGuest, is(guestForComparison));
        
        String guestName = guestForComparison.getFirstName(); 
        
        assertEquals("TestName", guestName);     
    	
    }
    
    
    @Test
	public void testDeleteGuest() {     
		
		when(guestRepository.save(any(Guest.class))).thenReturn(new Guest());            

		verify(guestRepository, never()).delete(any(Guest.class));  
 
	} 
    
    
    @Test
	public void testUpdateGuest() {    
    	
    	Guest testGuest = createTestGuest();
		
		when(guestRepository.save(any(Guest.class))).thenReturn(testGuest);      
		
		assertThat(guestService.updateGuest(testGuest), is(testGuest));     

		verify(guestRepository).save(any(Guest.class));      

		verify(guestRepository, times(1)).save(any(Guest.class));    
		
	}
    
    
	public Guest createTestGuest() {

		DateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");    
		String dateFromAsString = "2018-12-01 15-20-30";
		Date testDateFrom = null;
		try {
			testDateFrom = sourceFormat.parse(dateFromAsString);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		String dateToAsString = "2018-12-03 18-40-40";     
		Date testDateTo = null;
		try {
			testDateTo = sourceFormat.parse(dateToAsString);
		} catch (ParseException e) {
			e.printStackTrace();
		}      
		
		return new Guest(GUEST_ID, "TestName", "TestLastName", "TestVisitType", testDateFrom, testDateTo, 20,
		         "TestLocation", "TestManager", "TestComment", ""+(Math.random()*10000)+"", ""+(Math.random()*10000)+"");  
	} 
	
	public Agent createTestAgent() {
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		return new Agent("" + (Math.random() * 10000) + "", "TestAgentName", "TestAgentSurname", "+1234567890",
				"TestAddress", "Male", now, "");
	} 

}
