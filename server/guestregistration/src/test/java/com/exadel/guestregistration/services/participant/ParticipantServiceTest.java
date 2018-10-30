package com.exadel.guestregistration.services.participant;

import com.exadel.guestregistration.exceptions.EventFullException;
import com.exadel.guestregistration.exceptions.NotFoundException;
import com.exadel.guestregistration.models.*;
import com.exadel.guestregistration.repositories.*;
import com.exadel.guestregistration.services.ParticipantService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ParticipantServiceTest {

    @Autowired
    ParticipantService participantService;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    AgentRepository agentRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    OfficeRepository officeRepository;

    @Autowired
    ParticipantRepository participantRepository;

    private Event event = new Event("testname", "testmanager", "testlocation", new Date(2), new Date(3), 10, "Outside", true);
    private Agent agent = new Agent(null, "testname", "testsurname", "000", null, null, null, null);
    private Office office = new Office(null, "testname", "testaddress", "0000", "testemail", null, "testMName", "testMSurname", "testMEmail");
    private Card card;



    @Before
    public void setup() {
        Assert.assertNotNull(participantService);
        Assert.assertNotNull(eventRepository);
        Assert.assertNotNull(agentRepository);
        Assert.assertNotNull(cardRepository);
        Assert.assertNotNull(officeRepository);
        Assert.assertNotNull(participantRepository);
        event = eventRepository.save(event);
        office = officeRepository.save(office);
        agent = agentRepository.save(agent);
        card =  new Card(null, 1, new Date(), new Date(), office.getId(),"GUEST");
        card = cardRepository.save(card);
    }

    @After
    public void clean(){
        eventRepository.deleteAll();
        agentRepository.deleteAll();
        cardRepository.deleteAll();
        officeRepository.deleteAll();
        participantRepository.deleteAll();
    }

    private EventParticipant createParticipant() {
        return new EventParticipant(null, event.getId(), agent.getId(), null);
    }

    @Test
    public void addParticipantTest() throws Exception {
        EventParticipant participant = createParticipant();
        Assert.assertTrue(participantService.addParticipant(participant));
        List<EventParticipant> participants = participantRepository.findAll();
        Assert.assertNotNull(participants);
        Assert.assertTrue(participants.size() > 0);
        EventParticipant participantFound = participants.get(0);
        Assert.assertEquals(participantFound.getEventId(), participant.getEventId());
        Assert.assertEquals(participantFound.getAgentId(), participant.getAgentId());
    }

    @Test
    public void updateParticipantTest() throws Exception{
        EventParticipant participant = createParticipant();
        participant = participantRepository.save(participant);
        participant.setCardId(card.getId());
        participantService.updateParticipant(participant);
        EventParticipant foundParticipant = participantRepository.findById(participant.getId()).get();
        Assert.assertEquals(participant.getCardId(), foundParticipant.getCardId());
        Assert.assertEquals(foundParticipant.getEventId(), participant.getEventId());
        Assert.assertEquals(foundParticipant.getAgentId(), participant.getAgentId());
    }


    @Test
    public void getParticipantsInEventTest() throws Exception{
        ArrayList<EventParticipant> participants = new ArrayList<>();
        participants.add(createParticipant());
        participants.add(createParticipant());
        participants.add(createParticipant());
        participants.add(createParticipant());
        participantRepository.saveAll(participants);
        List<EventParticipant> foundParticipants = participantService.getParticipantsInEvent(event.getId());
        Assert.assertEquals(participants.size(), foundParticipants.size());
        for (int i = 0; i < foundParticipants.size(); i++) {
            Assert.assertEquals(participants.get(i).getEventId(),foundParticipants.get(i).getEventId());
            Assert.assertEquals(participants.get(i).getAgentId(),foundParticipants.get(i).getAgentId());
        }
    }

    @Test
    public void deleteParticipantTest(){
        EventParticipant participant = createParticipant();
        participant = participantRepository.save(participant);
        Assert.assertNotNull(participantRepository.findById(participant.getId()).get());
        participantService.deleteParticipant(participant.getId());
        Assert.assertFalse(participantRepository.findById(participant.getId()).isPresent());
    }

    @Test
    public void getAgentsNotInEventTest() throws Exception{
        Agent agent = new Agent(null, "testname", "testsurname", "000", null, null, null, null);
        Agent agent1 = new Agent(null, "testname1", "testsurname1", "0001", null, null, null, null);
        Agent agent2 = new Agent(null, "testname2", "testsurname2", "0002", null, null, null, null);
        Agent agent3 = new Agent(null, "testname3", "testsurname3", "0003", null, null, null, null);
        agent = agentRepository.save(agent);
        agent1 = agentRepository.save(agent1);
        agent2 = agentRepository.save(agent2);
        agent3 = agentRepository.save(agent3);
        participantRepository.save(new EventParticipant(null, event.getId(), agent.getId(), null));
        List<Agent> participants = participantService.getAgentsNotInEvent(event.getId());
        for (int i = 0; i < participants.size(); i++) {
            Assert.assertNotEquals(participants.get(i).getId(), agent.getId());
            Assert.assertTrue(agent1.getId().equals(participants.get(i).getId()) ||
                    agent2.getId().equals(participants.get(i).getId()) ||
                    agent3.getId().equals(participants.get(i).getId()) ||
                    this.agent.getId().equals(participants.get(i).getId()));
        }
    }

    @Test
    public void findAgentsNotInEventTest() throws Exception{
        Agent agent = new Agent(null, "testname", "testsurname", "000", null, null, null, null);
        Agent agent1 = new Agent(null, "testname1", "testsurname1", "0001", null, null, null, null);
        Agent agent2 = new Agent(null, "testname2", "testsurname2", "0002", null, null, null, null);
        Agent agent3 = new Agent(null, "testname3", "testsurname3", "0003", null, null, null, null);
        agent = agentRepository.save(agent);
        agent1 = agentRepository.save(agent1);
        agent2 = agentRepository.save(agent2);
        agent3 = agentRepository.save(agent3);
        participantRepository.save(new EventParticipant(null, event.getId(), agent.getId(), null));
        List<Agent> participants = participantService.findAgentsNotInEvent(event.getId(), agent2.getName());
        Assert.assertEquals(participants.size(), 1);
        Assert.assertNotEquals(participants.get(0).getId(), agent.getId());
        Assert.assertEquals(participants.get(0).getId(), agent2.getId());
    }
}
