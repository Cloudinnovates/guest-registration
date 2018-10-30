package com.exadel.guestregistration.services;

import static org.junit.Assert.*; 
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.exadel.guestregistration.exceptions.AgentException;
import com.exadel.guestregistration.exceptions.NotFoundException;
import com.exadel.guestregistration.exceptions.ParameterException;  
import com.exadel.guestregistration.models.Agent;
import com.exadel.guestregistration.models.Card;
import com.exadel.guestregistration.models.CardInfo;
import com.exadel.guestregistration.models.Guest;
import com.exadel.guestregistration.models.Office;
import com.exadel.guestregistration.repositories.GuestRepository; 

@RunWith(SpringRunner.class)
@SpringBootTest
public class CardInfoControllerTest {  
	
	private static final String CARD_INFO_ID = "ID-0";    
	private static final String AGENT_ID = "ID-1";   
	private static final String CARD_ID = "ID-2";    
	private static final String OFFICE_ID = "ID-3";    
	
	@Autowired
	private CardInfoService cardInfoService;
	
	@Autowired
	private GuestRepository guestRepository;
	
	@Autowired
	private GuestService guestService; 
	
	@Autowired
	private AgentService agentService;
	
	@Autowired
	private CardService cardService;
	
	@Autowired
	private OfficeService officeService;
	
	@Before
	public void setup() {
		Assert.assertNotNull(cardInfoService);     
		Assert.assertNotNull(guestRepository);       
		Assert.assertNotNull(agentService);        
		Assert.assertNotNull(cardService);          
		Assert.assertNotNull(officeService);            
	}
	
	@After
	public void clean() {
		guestRepository.deleteAll();    
	}
	
	
	@Test 
	public void testGetSearchedCards() throws AgentException,
										NotFoundException, ParameterException {          
		
		Guest testGuest = createTestGuest();
		
		Agent testAgent = createTestAgent();
		
		Office testOffice = createTestOffice();
		
		Card testCard = createTestCard();  
		
		
		guestService.createGuest(testGuest);  
		
		String guestCardId = testGuest.getCardId();   
		
		assertTrue(!guestRepository.findGuestsByField(guestCardId).isEmpty());   
		
		assertNotNull("should not be null", guestRepository.findGuestsByField(guestCardId));    
		
		List<Guest> guestForComparison = guestRepository.findGuestsByField(guestCardId);
		
		assertEquals("TestComment", guestForComparison.get(0).getComment());    
		
		
		
		agentService.create(testAgent);   
		
		Agent agentGuest =  agentService.findAgentById(testAgent.getId());    
		
		assertNotNull("agentGuest should not be null", agentGuest);   
		
		assertEquals("TestAgentName", agentGuest.getName());    
		
		
		
		officeService.create(testOffice);
		
		Office office = officeService.findOfficeById(testCard.getOfficeId());   
		
		assertNotNull("office should not be null", office);   
		
		assertEquals("TestOfficeName", office.getName());   
		
		
		
		cardService.createCard(testCard);
		
		Card card = cardService.getCard(guestCardId);     
		
		assertNotNull("card should not be null", card);   
		
		assertEquals("Guest", card.getCardType());   
		
		
		
		CardInfo testCardInfo = new CardInfo(CARD_INFO_ID, card.getCardNo(), agentGuest.getName(),
				                             agentGuest.getSurname(), card.getCardType(),
				                             office.getName(), card.getValidFrom(), card.getValidThru()); 
		
		
		assertEquals("Guest", testCardInfo.getHolderType()); 
		assertEquals(77, testCardInfo.getCardNumber()); 
		assertEquals("TestOfficeName", testCardInfo.getLocation());   
		assertEquals("TestAgentSurname", testCardInfo.getLastName());     
		
		List<CardInfo> testList = new ArrayList<CardInfo>();
		
		testList.add(testCardInfo);
		testList.add(testCardInfo);
		
		CardInfo cardInfoFromTestList = testList.get(1);    
		
		assertNotNull("testList should not be null", testList);   
		assertEquals(2, testList.size());  
		assertEquals("TestAgentName", cardInfoFromTestList.getFirstName());   
		assertEquals(77, cardInfoFromTestList.getCardNumber()); 
		
		
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
