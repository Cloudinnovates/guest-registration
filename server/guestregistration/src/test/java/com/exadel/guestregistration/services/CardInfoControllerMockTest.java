package com.exadel.guestregistration.services;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
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
import com.exadel.guestregistration.exceptions.AgentException;
import com.exadel.guestregistration.exceptions.NotFoundException;
import com.exadel.guestregistration.exceptions.ParameterException;
import com.exadel.guestregistration.models.Agent;
import com.exadel.guestregistration.models.Card;
import com.exadel.guestregistration.models.Guest;
import com.exadel.guestregistration.models.Office;
import com.exadel.guestregistration.repositories.AgentRepository;
import com.exadel.guestregistration.repositories.CardRepository;
import com.exadel.guestregistration.repositories.GuestRepository;
import com.exadel.guestregistration.repositories.OfficeRepository;
import com.exadel.guestregistration.services.impl.AgentServiceImpl;
import com.exadel.guestregistration.services.impl.CardServiceImpl;
import com.exadel.guestregistration.services.impl.GuestServiceImpl;
import com.exadel.guestregistration.services.impl.OfficeServiceImpl;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CardInfoControllerMockTestConfiguration.class)     
public class CardInfoControllerMockTest {
	
private static final String CARD_INFO_ID = "ID-0";        
private static final String AGENT_ID = "ID-1";   
private static final String CARD_ID = "ID-2";      
private static final String OFFICE_ID = "ID-3";  
    
    @Autowired
    private GuestRepository guestRepository; 

    @Autowired
    private GuestService guestService;
    
    @Autowired
    private AgentRepository agentRepository;
    
    @Autowired
    private AgentService agentService; 
    
    @Autowired
    private CardRepository cardRepository;  
    
    @Autowired
    private CardService cardService;
    
    @Autowired
    private OfficeRepository officeRepository;
    
    @Autowired
    private OfficeService officeService;   
      

    @Before
    public void setup() {   
         
    	List<Guest> guestList = Arrays.asList(createTestGuest());        
		Mockito.when(guestRepository.findGuestsByField(CARD_ID)).thenReturn(guestList);     
		
		Mockito.when(agentRepository.findById(AGENT_ID)).thenReturn(Optional.of(createTestAgent()));   
	
		Mockito.when(officeRepository.findById(OFFICE_ID)).thenReturn(Optional.of(createTestOffice()));   
		
		Mockito.when(cardRepository.findById(CARD_ID)).thenReturn(Optional.of(createTestCard()));                 
    	 
    }

    @After
    public void clean() {

    }

    @Test
    public void testLoadedServiceAndDAOs() {
        assertNotNull(guestRepository);
        assertNotNull(guestService);
        
        assertNotNull(agentRepository);
        assertNotNull(agentService);
        
        assertNotNull(cardRepository);
        assertNotNull(cardService);
        
        assertNotNull(officeRepository);
        assertNotNull(officeService); 
        

        assertTrue(guestService instanceof GuestServiceImpl);  
        assertTrue(agentService instanceof AgentServiceImpl);  
        assertTrue(cardService instanceof CardServiceImpl);  
        assertTrue(officeService instanceof OfficeServiceImpl);    

        assertTrue(guestRepository.getClass().getSimpleName().contains("$MockitoMock$"));   
        assertTrue(agentRepository.getClass().getSimpleName().contains("$MockitoMock$"));      
        assertTrue(cardRepository.getClass().getSimpleName().contains("$MockitoMock$"));      
        assertTrue(officeRepository.getClass().getSimpleName().contains("$MockitoMock$"));         

    }
    
    
    @Test 
	public void testGetSearchedCards() throws AgentException,
										NotFoundException, ParameterException {
    	
    	Guest testGuest = createTestGuest();   
		
		Agent testAgent = createTestAgent();
		
		Office testOffice = createTestOffice();
		
		Card testCard = createTestCard();
		
		                     
               
        List<Guest> guestList = guestRepository.findGuestsByField(testGuest.getCardId());                
        
        assertNotNull("should not be null", guestList);  
        
        String comparisonLastName = guestList.get(0).getLastName();      
       
        assertEquals("TestLastName", comparisonLastName);       
        
        String comparisonVisitType = guestList.get(0).getVisitType();     
        
        assertEquals("TestVisitType", comparisonVisitType);  
        
        
        
        Optional<Agent> agentGuest = agentRepository.findById(testAgent.getId());          
        
        
        //assertEquals(Optional.of("expected"), opt); 
        //assertThat(agentGuest, isPresent());
        //assertThat(agentGuest, hasProperty("gender"));            
        //assertThat(agentGuest, hasValue("gender"));  
//        assertThat(agentGuest, hasProperty("gender", is("Male")));        
//        
//        assertNotNull("should not be null", agentGuest);  
//        
//        assertSame("should be same", guestForComparison, guestForComparison);   
//        
//        assertThat(testGuest, is(guestForComparison));
//        
//        String guestName = guestForComparison.getFirstName(); 
//        
//        assertEquals("TestName", guestName);  
    	
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
		
		return new Guest(""+(Math.random()*10000)+"", "TestName", "TestLastName",
				         "TestVisitType", testDateFrom, testDateTo, 20,
		                 "TestLocation", "TestManager", "TestComment", CARD_ID, 
		                 AGENT_ID);     
	} 
	
	
	public Agent createTestAgent() {
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		return new Agent(AGENT_ID, "TestAgentName", "TestAgentSurname", "+1234567890",        
				"TestAddress", "Male", now, "");
	} 
	
	
	private Office createTestOffice() {
        return new Office(OFFICE_ID, "TestOfficeName", "TestAddress",  
                        "TestPhone", "TestEmail", "TestType",   
                        "TestFirstName", "TestSurname", "TestManagerEmail");    
    }
	
	
	private Card createTestCard(){
        return new Card(CARD_ID, 77,
        		        new Date(1537522766078L), new Date(1593162000000L), OFFICE_ID,  
        		        "Guest", true);     
    } 

}    
