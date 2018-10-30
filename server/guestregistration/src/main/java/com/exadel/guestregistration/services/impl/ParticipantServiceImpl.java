package com.exadel.guestregistration.services.impl;

import com.exadel.guestregistration.exceptions.EventFullException;
import com.exadel.guestregistration.exceptions.NotFoundException;
import com.exadel.guestregistration.models.Agent;
import com.exadel.guestregistration.models.Event;
import com.exadel.guestregistration.models.EventParticipant;
import com.exadel.guestregistration.repositories.EventRepository;
import com.exadel.guestregistration.repositories.ParticipantRepository;
import com.exadel.guestregistration.services.AgentService;
import com.exadel.guestregistration.services.EventService;
import com.exadel.guestregistration.services.ParticipantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ParticipantServiceImpl implements ParticipantService {
    private static final Logger logger = LoggerFactory.getLogger(ParticipantServiceImpl.class);

    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    AgentService agentService;

    @Autowired
    EventRepository eventRepository;

    @Override
    public EventParticipant updateParticipant(EventParticipant eventParticipant) throws NotFoundException {
        Optional<EventParticipant> foundParticipant = participantRepository.findById(eventParticipant.getId());
        if(foundParticipant.isPresent()){
            EventParticipant participant = foundParticipant.get();
            participant.setAgentId(eventParticipant.getAgentId());
            participant.setEventId(eventParticipant.getEventId());
            participant.setCardId(eventParticipant.getCardId());
            return participantRepository.save(participant);
        }else {
            throw new NotFoundException();
        }
    }

    @Override
    public boolean addParticipant(EventParticipant eventParticipant) throws EventFullException {
        if (!participantRepository.findEventParticipantsByBothId(eventParticipant.getEventId(), eventParticipant.getAgentId()).isPresent()){
            if(eventRepository.findById(eventParticipant.getEventId()).isPresent()){
                if(eventRepository.findById(eventParticipant.getEventId()).get().getParticipantNo() > participantRepository.findEventParticipants(eventParticipant.getEventId()).size()){
                    participantRepository.save(eventParticipant);
                    return true;
                }else{
                    logger.debug("event with id: "+eventParticipant.getEventId()+", is full");
                    throw new EventFullException("event with id: "+eventParticipant.getEventId()+", is full");
                }
            }

            return false;
        }
        return false;
    }

    @Override
    public List<EventParticipant> getParticipantsInEvent(String eventId) throws NotFoundException {
        return participantRepository.findEventParticipants(eventId);
    }

    @Override
    public void deleteParticipant(String id) {
            participantRepository.deleteById(id);
    }

    @Override
    public List<Agent> getAgentsNotInEvent(String eventId) throws NotFoundException {
        List<EventParticipant> participants = getParticipantsInEvent(eventId);
        List<Agent> agents = new ArrayList<>();
        for (EventParticipant participant : participants) {
            agents.add(agentService.findAgentById(participant.getAgentId()));
        }
        return getDiffInArrays(agentService.findAll(), agents);
    }

    @Override
    public List<Agent> findAgentsNotInEvent(String eventId, String text) throws NotFoundException {
        List<EventParticipant> participants = getParticipantsInEvent(eventId);
        List<Agent> agents = new ArrayList<>();
        for (EventParticipant participant : participants) {
            agents.add(agentService.findAgentById(participant.getAgentId()));
        }
        return getDiffInArrays(agentService.findAgents(text), agents);
    }

    private List<Agent> getDiffInArrays(List<Agent> array1 ,List<Agent> array2){
        List<Agent> result = new ArrayList<>();
        for (int i = 0; i < array1.size(); i++) {
            boolean found = false;
            for (int j = 0; j < array2.size() ; j++) {
                if(array1.get(i).getId().equals(array2.get(j).getId())){
                    found = true;
                }
            }
            if(!found){
                result.add(array1.get(i));
            }
        }

        return result;
    }
}
