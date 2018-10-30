package com.exadel.guestregistration.services.impl;

import com.exadel.guestregistration.exceptions.AgentException;
import com.exadel.guestregistration.exceptions.NotFoundException;
import com.exadel.guestregistration.models.Agent;
import com.exadel.guestregistration.repositories.AgentRepository;
import com.exadel.guestregistration.services.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentServiceImpl implements AgentService {

    @Autowired
    private AgentRepository agentRepository;

    @Override
    public List<Agent> findAll() {
        return agentRepository.findAll();
    }

    @Override
    public Agent create(Agent agent) throws AgentException {
        if (!checkAgent(agent.getName(), agent.getSurname())) {
            return agentRepository.insert(agent);
        } else {
            throw new AgentException("The agent with the same credentials already exist");
        }
    }

    @Override
    public Agent update(Agent agent) throws AgentException, NotFoundException {
        if (agentRepository.findById(agent.getId()).isPresent()) {
            if (!(agentRepository.findById(agent.getId()).get().getName().equals(agent.getName()) &&
                    agentRepository.findById(agent.getId()).get().getSurname().equals(agent.getSurname()))) {
                if (!checkAgent(agent.getName(), agent.getSurname())) {
                    Agent agentData = agentRepository.findById(agent.getId()).get();
                    agentData.setName(agent.getName());
                    agentData.setSurname(agent.getSurname());
                    agentData.setPhone(agent.getPhone());
                    agentData.setAddress(agent.getAddress());
                    agentData.setGender(agent.getGender());
                    agentData.setDate_of_birth(agent.getDate_of_birth());
                    agentData.setWork_place(agent.getWork_place());
                    return agentRepository.save(agentData);
                } else {
                    throw new AgentException("The agent with the same credentials already exist");
                }
            } else {
                Agent agentData = agentRepository.findById(agent.getId()).get();
                agentData.setPhone(agent.getPhone());
                agentData.setAddress(agent.getAddress());
                agentData.setGender(agent.getGender());
                agentData.setDate_of_birth(agent.getDate_of_birth());
                agentData.setWork_place(agent.getWork_place());
                return agentRepository.save(agentData);
            }
        } else {
            throw new NotFoundException("Agent with the provided id not found!");
        }
    }

    @Override
    public void delete(String id) throws NotFoundException {
        if(agentRepository.findById(id).isPresent()) {
            agentRepository.deleteById(id);
        } else {
            throw new NotFoundException("Agent with the provided id not found!");
        }
    }

    @Override
    public Agent findAgentById(String id) throws NotFoundException {
        if (agentRepository.findById(id).isPresent()) {
            return agentRepository.findById(id).get();
        } else {
            throw new NotFoundException("Agent with the provided id not found!");
        }
    }

    @Override
    public List<Agent> findAgents(String text) {
     return agentRepository.findAgentsByText(text);
    }

    @Override
    public boolean checkAgent(String name, String surname) {
        Agent agent = agentRepository.checkAgentByParameters(name, surname);
        return agent != null;
    }
}
