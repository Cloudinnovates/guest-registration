package com.exadel.guestregistration.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Document(collection = "participants")
public class EventParticipant {
    @Id
    private String id;

    @NotNull
    @NotEmpty
    private String eventId;

    @NotNull
    @NotEmpty
    private String agentId;

    private String cardId;

    public EventParticipant() {
        super();
    }

    public EventParticipant(String id, @NotNull @NotEmpty String eventId, @NotNull @NotEmpty String agentId, String cardId) {
        this.id = id;
        this.eventId = eventId;
        this.agentId = agentId;
        this.cardId = cardId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    @Override
    public String toString() {
        return "EventParticipant{" +
                "id='" + id + '\'' +
                ", eventId='" + eventId + '\'' +
                ", agentId='" + agentId + '\'' +
                ", cardId='" + cardId + '\'' +
                '}';
    }
}
