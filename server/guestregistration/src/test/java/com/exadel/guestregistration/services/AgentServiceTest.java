package com.exadel.guestregistration.services;

import com.exadel.guestregistration.exceptions.NotFoundException;
import com.exadel.guestregistration.models.Agent;
import com.exadel.guestregistration.repositories.AgentRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AgentServiceTest {

    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    private AgentService agentService;

    @Before
    public void setup() {
        Assert.assertNotNull(agentService);
        Assert.assertNotNull(agentRepository);
    }

    @Test
    public void findAll() {
        Agent newAgent = newAgent();
        agentRepository.save(newAgent);
        List<Agent> agents = agentRepository.findAll();

        Assert.assertNotNull(agents);
        Assert.assertEquals(agents.size(), agentRepository.findAll().size());

        Agent agent = newExistingAgent(newAgent.getName() + 's', newAgent.getSurname() + 's');
        Assert.assertFalse(agentService.checkAgent(agent.getName(), agent.getSurname()));
        agentRepository.save(agent);

        Assert.assertEquals(agents.size() + 1, agentRepository.findAll().size());
        agentRepository.deleteById(agent.getId());
        Assert.assertEquals(agents.size(), agentRepository.findAll().size());

        agent = newExistingAgent(agentRepository.findAll().get(0).getName(), agentRepository.findAll().get(0).getSurname());
        Assert.assertTrue(agentService.checkAgent(agent.getName(), agent.getSurname()));

        agentRepository.deleteById(newAgent.getId());
    }

    @Test
    public void create() throws NotFoundException {
        Agent agent = newAgent();
        Assert.assertFalse(agentService.checkAgent(agent.getName(), agent.getSurname()));
        agentRepository.save(agent);
        Assert.assertTrue(agentService.checkAgent(agent.getName(), agent.getSurname()));
        Agent foundAgent = agentService.findAgentById(agent.getId());

        Assert.assertNotNull(foundAgent);

        Assert.assertNotNull(foundAgent.getName());
        Assert.assertNotNull(foundAgent.getSurname());
        Assert.assertNotNull(foundAgent.getPhone());

        Assert.assertEquals(agent.getName(), foundAgent.getName());
        Assert.assertEquals(agent.getSurname(), foundAgent.getSurname());
        Assert.assertEquals(agent.getPhone(), foundAgent.getPhone());
        Assert.assertEquals(agent.getAddress(), foundAgent.getAddress());
        Assert.assertEquals(agent.getDate_of_birth(), foundAgent.getDate_of_birth());
        Assert.assertEquals(agent.getWork_place(), foundAgent.getWork_place());

        agentRepository.deleteById(agent.getId());
    }

    @Test
    public void update() throws NotFoundException {
        Agent firstAgent = newAgent();
        Agent secondAgent = newAgent();
        Assert.assertNotEquals(firstAgent.getId(), secondAgent.getId());

        secondAgent.setId(firstAgent.getId());
        Assert.assertEquals(firstAgent.getId(), secondAgent.getId());

        secondAgent.setId("1234567890");
        agentRepository.save(firstAgent);
        agentRepository.save(secondAgent);

        Assert.assertTrue(agentRepository.findById(secondAgent.getId()).isPresent());
        Assert.assertNotEquals(firstAgent.getId(), agentService.findAgentById(secondAgent.getId()).getId());
        Assert.assertEquals(firstAgent.getName(), secondAgent.getName());

        List<Agent> agents = agentRepository.findAll();
        secondAgent.setName(agents.get(0).getName());
        secondAgent.setSurname(agents.get(0).getSurname());
        Assert.assertTrue(agentService.checkAgent(secondAgent.getName(), secondAgent.getSurname()));
        secondAgent.setSurname(agents.get(0).getSurname() + 's');
        Assert.assertFalse(agentService.checkAgent(secondAgent.getName(), secondAgent.getSurname()));

        agentRepository.deleteById(firstAgent.getId());
        agentRepository.deleteById(secondAgent.getId());
    }

    @Test
    public void delete() throws NotFoundException {
        Agent agent = newAgent();
        agentRepository.save(agent);

        Assert.assertTrue(agentRepository.findById(agent.getId()).isPresent());
        agentService.delete(agent.getId());
        Assert.assertFalse(agentRepository.findById(agent.getId()).isPresent());
    }

    @Test
    public void findAgentById() throws NotFoundException {
        Agent agent = newAgent();
        Assert.assertFalse(agentService.checkAgent(agent.getName(), agent.getSurname()));
        agentRepository.save(agent);
        Agent foundAgent = agentService.findAgentById(agent.getId());
        Assert.assertNotNull(foundAgent);

        Assert.assertNotNull(foundAgent.getName());
        Assert.assertEquals(foundAgent.getName(), agent.getName());
        Assert.assertNotNull(foundAgent.getSurname());
        Assert.assertEquals(foundAgent.getSurname(), agent.getSurname());
        Assert.assertNotNull(foundAgent.getPhone());
        Assert.assertEquals(foundAgent.getPhone(), agent.getPhone());
        Assert.assertEquals(foundAgent.getAddress(), agent.getAddress());
        Assert.assertEquals(foundAgent.getGender(), agent.getGender());
        Assert.assertEquals(foundAgent.getDate_of_birth(), agent.getDate_of_birth());
        Assert.assertEquals(foundAgent.getWork_place(), agent.getWork_place());

        agentRepository.deleteById(agent.getId());
    }

    @Test
    public void findAgents() throws NotFoundException {
        Agent firstAgent = newAgent();
        Agent secondAgent = newExistingAgent(firstAgent.getName() + 's', firstAgent.getSurname() + 's');
        agentRepository.save(firstAgent);
        Assert.assertTrue(agentService.checkAgent(firstAgent.getName(), firstAgent.getSurname()));
        Assert.assertFalse(agentService.checkAgent(secondAgent.getName(), secondAgent.getSurname()));
        agentRepository.save(secondAgent);

        List<Agent> agents = agentRepository.findAgentsByText(firstAgent.getGender());
        Assert.assertFalse(agents.isEmpty());
        Assert.assertTrue(agents.size() > 1);

        Agent agent = agentService.findAgentById(agents.get(0).getId());
        Assert.assertNotNull(agent);

        Assert.assertTrue(agentService.checkAgent(agents.get(0).getName(), agents.get(0).getSurname()));

        Assert.assertNotSame(agents.get(0), agents.get(1));

        agentRepository.deleteById(firstAgent.getId());
        agentRepository.deleteById(secondAgent.getId());
    }

    @Test
    public void checkAgent() {
        Agent agent = newAgent();
        Assert.assertFalse(agentService.checkAgent(agent.getName(), agent.getSurname()));
        agentRepository.save(agent);
        Agent anotherAgent = newExistingAgent(agent.getName(), agent.getSurname());
        Assert.assertTrue(agentService.checkAgent(anotherAgent.getName(), anotherAgent.getSurname()));

        Assert.assertEquals(agent.getName(),anotherAgent.getName());
        Assert.assertEquals(agent.getSurname(),anotherAgent.getSurname());

        anotherAgent.setName(anotherAgent.getName() + 's');
        Assert.assertNotEquals(agent.getName(), anotherAgent.getName());
        Assert.assertFalse(agentService.checkAgent(anotherAgent.getName(), anotherAgent.getSurname()));

        agentRepository.deleteById(agent.getId());
    }

    private Agent newAgent() {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        return new Agent(""+(Math.random()*10000)+"", "Sam", "Samson", "+1234567890",
                "Address", "Male", now,"");
    }

    private Agent newExistingAgent(String name, String surname) {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        return new Agent(""+(Math.random()*10000)+"", name, surname, "+1234567890",
                "Address", "Male", now,"");
    }
}