package com.exadel.guestregistration.repositories;

import com.exadel.guestregistration.models.EventParticipant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends MongoRepository<EventParticipant, String> {
    @Query("{'eventId': ?0, 'agentId': ?1}")
    Optional<EventParticipant> findEventParticipantsByBothId(String eventId, String agentId);

    @Query("{'eventId': ?0}")
    List<EventParticipant> findEventParticipants(String eventId);
}
