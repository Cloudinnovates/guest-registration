package com.exadel.guestregistration.repositories;

import com.exadel.guestregistration.models.Agent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface AgentRepository extends MongoRepository<Agent, String> {

    @Query("{$or: [{'name': {$regex: ?0, $options: 'i'}}, {'surname': {$regex: ?0, $options: 'i'}}," +
            " {'phone': {$regex: ?0, $options: 'i'}}, {'address': {$regex: ?0, $options: 'i'}}," +
            " {'gender': {$regex: ?0, $options: 'i'}}, {'work_place': {$regex: ?0, $options: 'i'}}]}")
    List<Agent> findAgentsByText(String text);

    @Query("{'name': {$regex: '^?0$', $options: 'i'}, 'surname': {$regex: '^?1$', $options: 'i'}}")
    Agent checkAgentByParameters(String name, String surname);
}
