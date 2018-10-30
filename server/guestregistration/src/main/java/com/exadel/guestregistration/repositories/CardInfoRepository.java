package com.exadel.guestregistration.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.exadel.guestregistration.models.CardInfo;

@Repository
public interface CardInfoRepository extends MongoRepository<CardInfo, String> {   
	
	@Query(value = "{ $or: [ {'eventName' : { $regex : ?0, $options: 'i' } },"
		         + "{'manager' : { $regex : ?0, $options: 'i' } }," 
                 + "{'location' : { $regex : ?0, $options: 'i' } },"
                 + "{'eventType' : { $regex : ?0, $options: 'i' } } ] }")     
    List<CardInfo> findCardInfosByField(String searchingCardInfo);                               


    @Query("{ 'cardNumber' : ?0 }")	        
    List<CardInfo> findCardInfosByCardNumber(int number);  

}
