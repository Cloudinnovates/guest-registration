package com.exadel.guestregistration.services;

import com.exadel.guestregistration.exceptions.EventFullException;
import com.exadel.guestregistration.exceptions.NotFoundException;
import com.exadel.guestregistration.models.Agent;
import com.exadel.guestregistration.models.EventParticipant;

import java.util.List;

public interface ParticipantService {

    EventParticipant updateParticipant(EventParticipant eventParticipant) throws NotFoundException;
    boolean addParticipant(EventParticipant eventParticipant) throws EventFullException;
    List<EventParticipant> getParticipantsInEvent(String eventId) throws NotFoundException;
    void deleteParticipant(String id);
    List<Agent> getAgentsNotInEvent(String eventId) throws NotFoundException;
    List<Agent> findAgentsNotInEvent(String eventId, String text) throws NotFoundException;
}
