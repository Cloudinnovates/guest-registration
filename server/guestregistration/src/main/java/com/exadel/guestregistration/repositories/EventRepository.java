package com.exadel.guestregistration.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.exadel.guestregistration.models.Event;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {   
		
	@Query(value = "{ $or: [ {'eventName' : { $regex : ?0, $options: 'i' } },"
			     + "{'manager' : { $regex : ?0, $options: 'i' } }," 
                 + "{'location' : { $regex : ?0, $options: 'i' } },"
                 + "{'eventType' : { $regex : ?0, $options: 'i' } } ] }")     
	List<Event> findEventsByField(String searchingEvent);                           
	
	
	@Query("{ 'participantNo' : ?0 }")	        
	List<Event> findEventsByParticipantNumber(int number);     
	
	
	@Query("{ 'dateTo' : { $lt: ?0 } }")
	List<Event> findEventsByDateTo(Date dateToLT);  
	
	@Query("{ 'dateFrom' : { $gt: ?0 } }")
	List<Event> findEventsByDateFrom(Date dateFromGT);  
	
	@Query("{ 'dateFrom' : ?0 }")
	List<Event> findEventsByStartDate(Date dateOne); 
	
	@Query("{ 'dateTo' : ?0 }")
	List<Event> findEventsByEndDate(Date dateTwo);    

	@Query("{ 'isPrivate': false}")
  	List<Event> findPublicEvents();

	@Query("{'isPrivate': false, 'dateFrom': {$lte: ?0}, 'dateTo': {$gte: ?0}}")
	List<Event> findPublicCurrentEvents(Date date);

	@Query("{'dateFrom': {$lte: ?0}, 'dateTo': {$gte: ?0}}")
	List<Event> findCurrentEvents(Date date);


} 

