package com.exadel.guestregistration.services;

import static org.hamcrest.CoreMatchers.is;


import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.exadel.guestregistration.models.Agent;
import com.exadel.guestregistration.models.Guest;
import com.exadel.guestregistration.repositories.AgentRepository;
import com.exadel.guestregistration.repositories.GuestRepository; 

@RunWith(SpringRunner.class)
@SpringBootTest
public class GuestServiceTest {

	@Autowired
	private GuestService guestService; 
	
	@Autowired
	private AgentService agentService;
	
	@Autowired
	private GuestRepository guestRepository;  
	
	@Autowired
	private AgentRepository agentRepository;
	
	
	@Before
	public void setup() {
		Assert.assertNotNull(guestService);     
		Assert.assertNotNull(agentService);       
	}  
	
	@After
	public void clean() {
		guestRepository.deleteAll();
		agentRepository.deleteAll();   
	}
	 

	@Test 
	public void testGetGuests() throws Exception {    
		
		Guest testGuest = createTestGuest();
		guestService.createGuest(testGuest);   
		guestService.createGuest(testGuest);    

		List<Guest> actual = guestService.getGuests();
		int actualSize = actual.size();
		assertThat(actual.size(), is(actualSize));

		actual = guestService.getGuests();
		List<Guest> expected = guestService.getGuests();
		assertThat(actual, is(expected));  

	}  
	

	@Test
	public void testGetSearchedGuests() throws Exception {  
		
		Guest testGuest = createTestGuest();
		guestService.createGuest(testGuest);
		guestService.createGuest(testGuest);
		
		List<Guest> actual = guestService.getSearchedGuests("TestVisitType");
		List<Guest> expected = guestService.getSearchedGuests("TestVisitType");  
		
		assertNotNull("actual should not be null", actual);

		assertThat(actual, is(expected));  
		
		String guestComment = actual.get(0).getComment();
		String guestVisitType = actual.get(0).getVisitType(); 
		
		assertEquals("TestComment", guestComment);  
		assertEquals("TestVisitType", guestVisitType);             
		
	}
	
	
	@Test
	public void testGetSearchedParticipants() throws AgentException {      
		
		Agent testAgentOne = createTestAgent(); 
 
		agentService.create(testAgentOne);      
		
		List<Agent> actual = guestService.getSearchedParticipants("TestAgentName");
		List<Agent> expected = guestService.getSearchedParticipants("TestAgentName");          

		assertThat(actual.toString(), is(expected.toString()));           
		
		int actualSize = actual.size();
		
		assertThat(actual.size(), is(actualSize)); 
	   
	} 
	

	@Test
	public void testCreateGuest() throws Exception {     
		
		Guest testGuest = createTestGuest();
		guestService.createGuest(testGuest);

		Guest guestForComparison = guestService.getGuest(testGuest.getId());   
		
		assertNotNull("guestForComparison should not be null", guestForComparison);
		assertThat(guestForComparison, hasProperty("visitType", is("TestVisitType")));  
		assertThat(guestForComparison, hasProperty("comment", is("TestComment")));  
	} 
	

	@Test   
	public void testGetGuest() throws Exception { 
		
		Guest testGuest = createTestGuest();
		guestService.createGuest(testGuest);

		Guest guestForComparison = guestService.getGuest(testGuest.getId());
		assertNotNull("guestForComparison should not be null", guestForComparison);

		assertThat(guestForComparison, hasProperty("visitType", is("TestVisitType")));     
		
	}
	

	@Test
	public void testDeleteGuest() throws Exception { 
		
		Guest testGuest = createTestGuest();   
		guestService.createGuest(testGuest); 

		List<Guest> guestsBeforeDelete = guestService.getGuests();
		int sizeBeforeDelete = guestsBeforeDelete.size();
		assertEquals(guestsBeforeDelete.size(), sizeBeforeDelete); 

		guestService.deleteGuest(testGuest.getId()); 

		List<Guest> guestsAfterDelete = guestService.getGuests();
		int sizeAfterDelete = guestsAfterDelete.size();
		assertEquals(guestsBeforeDelete.size() - 1, sizeAfterDelete);      
	}
	

	@Test
	public void testUpdateGuest() throws Exception {  
		
		Guest testGuest = createTestGuest();
		guestService.createGuest(testGuest);

		Guest guestBeforeUpdate = guestService.getGuest(testGuest.getId());
		Guest guestForComparison = guestService.getGuest(testGuest.getId());

		guestBeforeUpdate.setComment("Some new comment");    
		guestService.updateGuest(guestBeforeUpdate); 

		Guest updatedGuest = guestService.getGuest(guestBeforeUpdate.getId());  
		
		assertEquals("Some new comment", updatedGuest.getComment()); 
		assertEquals("TestComment", guestForComparison.getComment());       
	 
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

		return new Guest(""+(Math.random()*10000)+"", "TestName", "TestLastName", "TestVisitType", testDateFrom, testDateTo, 20,
				         "TestLocation", "TestManager", "TestComment", ""+(Math.random()*10000)+"", ""+(Math.random()*10000)+"");          
	} 
	
	public Agent createTestAgent() {
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		return new Agent("" + (Math.random() * 10000) + "", "TestAgentName", "TestAgentSurname", "+1234567890",
				"TestAddress", "Male", now, "");
	} 
	
	

} 
