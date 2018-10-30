package com.exadel.guestregistration.repositories;

import com.exadel.guestregistration.models.Employee;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface EmployeeRepository extends MongoRepository<Employee, String> {

    @Query("{'agentId': ?0}")
    Employee checkEmployeeByParameter(String agentId);
    
    @Query("{'cardId': ?0}")
    List<Employee> findEmployeeByCardId(String cardId);      

}
