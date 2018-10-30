package com.exadel.guestregistration.controllers;

import com.exadel.guestregistration.exceptions.AgentException;
import com.exadel.guestregistration.exceptions.NotFoundException;
import com.exadel.guestregistration.models.Agent;
import com.exadel.guestregistration.services.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class AgentController {

    @Autowired
    private AgentService agentService;

    @GetMapping("/agent/getAll")
    public List<Agent> getAllAgents() {
        return agentService.findAll();
    }

    @PostMapping("/agent/create")
    public ResponseEntity<?> createAgent(@Valid @RequestBody Agent agent) {
        try {
            return ResponseEntity.ok().body(agentService.create(agent));
        } catch (AgentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("The agent with the same credentials already exist");
        }
    }

    @GetMapping(value = "/agent/{id}")
    public ResponseEntity<?> getAgentById(@PathVariable("id") String id) {
        try {
            return ResponseEntity.ok().body(agentService.findAgentById(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agent was not found");
        }
    }

    @PutMapping(value = "/agent/update/{id}")
    public ResponseEntity<?> updateAgent(@PathVariable("id") String id, @Valid @RequestBody Agent agent) {
        try {
            return ResponseEntity.ok().body(agentService.update(agent));
        } catch (AgentException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("The agent with the same credentials already exist");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agent was not found");
        }
    }

    @DeleteMapping(value = "/agent/delete/{id}")
    public ResponseEntity<?> deleteAgent(@PathVariable("id") String id) {
        try {
            agentService.delete(id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agent was not found");
        }
    }

    @GetMapping("/agent/find/{text}")
    public List<Agent> getAgents(@PathVariable("text") String text) {
        return agentService.findAgents(text);
    }

    @GetMapping("/agent/check/{name}/{surname}")
    public boolean checkAgent(@PathVariable("name") String name, @PathVariable("surname") String surname) {
        return agentService.checkAgent(name, surname);
    }
}
