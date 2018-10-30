package com.exadel.guestregistration.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.exadel.guestregistration.models.Event;
import com.exadel.guestregistration.models.Guest;

@Repository
public interface GuestRepository extends MongoRepository<Guest, String> {     
		
	@Query(value = "{ $or: [ {'firstName' : { $regex : ?0, $options: 'i' } },"
			              + "{'lastName' : { $regex : ?0, $options: 'i' } }," 
                          + "{'phone' : { $regex : ?0, $options: 'i' } },"
                          + "{'comment' : { $regex : ?0, $options: 'i' } },"
                          + "{'cardId' : { $regex : ?0, $options: 'i' } },"
                          + "{'visitType' : { $regex : ?0, $options: 'i' } } ] }")         
	List<Guest> findGuestsByField(String searchingGuest);                             
	
	
	@Query("{ 'participantNo' : ?0 }")	        
	List<Guest> findGuestsByParticipantNumber(int number);     
	
	
	@Query("{ 'dateTo' : { $lt: ?0 } }")
	List<Guest> findGuestsByDateTo(Date dateToLT);   
	
	
	@Query("{ 'dateFrom' : ?0 }")
	List<Guest> findGuestsByStartDate(Date dateOne);  
	
	@Query("{ 'dateTo' : ?0 }")
	List<Guest> findGuestsByEndDate(Date dateTwo);           
	   
  

} 


