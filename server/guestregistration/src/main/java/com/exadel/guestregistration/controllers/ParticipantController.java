package com.exadel.guestregistration.controllers;

import com.exadel.guestregistration.exceptions.EventFullException;
import com.exadel.guestregistration.exceptions.NotFoundException;
import com.exadel.guestregistration.exceptions.ParameterException;
import com.exadel.guestregistration.models.Agent;
import com.exadel.guestregistration.models.EventParticipant;
import com.exadel.guestregistration.services.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/participants")
@CrossOrigin(origins = "*")
public class ParticipantController {
    @Autowired
    private ParticipantService participantService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getParticipantsInEvent(@PathVariable("id") String eventId) throws NotFoundException {
        return ResponseEntity.ok().body(participantService.getParticipantsInEvent(eventId));
    }

    @PostMapping
    public ResponseEntity<?> postParticipant(@Valid @RequestBody EventParticipant eventParticipant){
        try {
            if (participantService.addParticipant(eventParticipant)){
                return ResponseEntity.ok().build();
            }else {
                return ResponseEntity.badRequest().build();
            }
        } catch (EventFullException e) {
            return ResponseEntity.ok().body(e.getMessage());
        }

    }

    @PutMapping()
    public ResponseEntity<?> updateParticipant(@Valid @RequestBody EventParticipant participant){
        try {
            return ResponseEntity.ok().body(participantService.updateParticipant(participant));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(
            params = {"id"},
            method = RequestMethod.DELETE
    )
    public ResponseEntity<?> deleteParticipant(@RequestParam("id") String id){
        participantService.deleteParticipant(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/free/{id}")
    public ResponseEntity<List<Agent>> getAgentsNotInEvent(@PathVariable("id") String eventId) throws NotFoundException {
        return ResponseEntity.ok().body(participantService.getAgentsNotInEvent(eventId));
    }

    @RequestMapping(
            value = "/find",
            params = {"eventId","text"},
            method = RequestMethod.GET
    )
    public ResponseEntity<List<Agent>> findAgentsNotInEvent(@RequestParam("eventId") String eventId, @RequestParam("text") String text) throws NotFoundException {
        return ResponseEntity.ok().body(participantService.findAgentsNotInEvent(eventId, text));
    }
}
