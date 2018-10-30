package com.exadel.guestregistration.services;

import com.exadel.guestregistration.exceptions.AgentException;
import com.exadel.guestregistration.exceptions.NotFoundException;
import com.exadel.guestregistration.models.Agent;
import java.util.List;

public interface AgentService {
    List<Agent> findAll();
    Agent create(Agent agent) throws AgentException;
    Agent update(Agent agent) throws AgentException, NotFoundException;
    void delete(String id) throws NotFoundException;
    Agent findAgentById(String id) throws NotFoundException;
    List<Agent> findAgents(String text);
    boolean checkAgent(String name, String surname);

}
