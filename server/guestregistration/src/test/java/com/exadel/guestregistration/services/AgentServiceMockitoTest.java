package com.exadel.guestregistration.services;

import com.exadel.guestregistration.exceptions.AgentException;
import com.exadel.guestregistration.exceptions.NotFoundException;
import com.exadel.guestregistration.models.Agent;
import com.exadel.guestregistration.repositories.AgentRepository;
import com.exadel.guestregistration.services.impl.AgentServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AgentServiceMockitoTest {

    @Mock
    private AgentRepository agentRepositoryMock;

    @InjectMocks
    private AgentServiceImpl agentService;

    @Captor
    private ArgumentCaptor<Agent> agentArgumentCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        agentArgumentCaptor = ArgumentCaptor.forClass(Agent.class);
    }

    @Test
    public void findAll() {
        if(agentRepositoryMock.findAll().size() == 0) {
            when(agentRepositoryMock.findAll()).thenReturn(null);
        }

        Agent firstAgent = newAgent();
        Agent secondAgent = newAgent();
        secondAgent.setName(secondAgent.getName() + 's');
        secondAgent.setSurname(secondAgent.getSurname() + 's');
        assertNotEquals(firstAgent, secondAgent);
        assertNotEquals(firstAgent.getName(), secondAgent.getName());

        when(agentRepositoryMock.findAll()).thenReturn(Arrays.asList(firstAgent, secondAgent));

        List<Agent> agents = agentService.findAll();
        assertFalse(agents.isEmpty());
        assertEquals(2, agents.size());
        verify(agentRepositoryMock, times(2)).findAll();
        verifyNoMoreInteractions(agentRepositoryMock);
    }

    @Test
    public void create() {
        Agent agent = newAgent();
        if (!agentService.checkAgent(agent.getName(), agent.getSurname())) {
            agentRepositoryMock.save(agent);
            when(agentRepositoryMock.save(any(Agent.class))).thenReturn(new Agent());
        } else {
            when(agentRepositoryMock.save(agent)).thenThrow(new AgentException("The agent with the same credentials already exist"));
        }

        verify(agentRepositoryMock, times(1)).save(any(Agent.class));
        assertThat(agentRepositoryMock.save(agent), is(notNullValue()));
        verify(agentRepositoryMock, never()).delete(any(Agent.class));

        verify(agentRepositoryMock, times(2)).save(agentArgumentCaptor.capture());

        assertNotNull(agentArgumentCaptor.getValue());
        assertNotNull(agentArgumentCaptor.getValue().getId());
        assertEquals(agent.getName(), agentArgumentCaptor.getValue().getName());
        assertEquals(agent.getSurname(), agentArgumentCaptor.getValue().getSurname());
        assertEquals(agent.getPhone(), agentArgumentCaptor.getValue().getPhone());
        assertEquals(agent.getAddress(), agentArgumentCaptor.getValue().getAddress());
        assertEquals(agent.getDate_of_birth(), agentArgumentCaptor.getValue().getDate_of_birth());
        assertEquals(agent.getGender(), agentArgumentCaptor.getValue().getGender());
        assertEquals(agent.getWork_place(), agentArgumentCaptor.getValue().getWork_place());
    }

    @Test
    public void update() throws NotFoundException, AgentException {
        Agent agent = newAgent();
        agentRepositoryMock.save(agent);
        when(agentRepositoryMock.save(any(Agent.class))).thenReturn(new Agent());
        when(agentRepositoryMock.findById(agent.getId())).thenReturn(java.util.Optional.of(agent));
        assertTrue(agentRepositoryMock.findById(agent.getId()).isPresent());

        Agent foundAgent = agentRepositoryMock.findById(agent.getId()).get();
        assertNotNull(foundAgent);
        assertEquals(agent, foundAgent);
        foundAgent.setName("Peter");
        foundAgent.setSurname("Parker");
        foundAgent.setAddress("Street No. 1");
        if (!agentService.checkAgent(foundAgent.getName(), foundAgent.getSurname())) {
            agentRepositoryMock.save(foundAgent);
            when(agentService.update(foundAgent)).thenReturn(new Agent());

            when(agentRepositoryMock.findById(foundAgent.getId())).thenReturn(java.util.Optional.of(foundAgent));
            assertEquals(foundAgent.getName(), "Peter");
            assertEquals(foundAgent.getSurname(), "Parker");
            assertEquals(foundAgent.getAddress(), "Street No. 1");
        } else {
            when(agentService.update(foundAgent)).thenThrow(new AgentException("The agent with the same credentials already exist"));
        }

        verify(agentRepositoryMock, times(2)).save(any(Agent.class));
    }

    @Test
    public void delete() throws NotFoundException {
        Agent agent = newAgent();
        when(agentRepositoryMock.findById(agent.getId())).thenReturn(java.util.Optional.of(agent));
        assertNotNull(agentRepositoryMock.findById(agent.getId()));

        doNothing().when(agentRepositoryMock).deleteById(agent.getId());
        agentService.delete(agent.getId());

        verify(agentRepositoryMock, times(1)).deleteById(agent.getId());
    }


    @Test
    public void findAgentById() {
        if (agentRepositoryMock.findAll().size() == 0) {
            when(agentRepositoryMock.findById(anyString())).thenReturn(null);
        }

        Agent agent = newAgent();
        when(agentRepositoryMock.findById(agent.getId())).thenReturn(java.util.Optional.of(agent));
        assertTrue(agentRepositoryMock.findById(agent.getId()).isPresent());

        Agent foundAgent = agentRepositoryMock.findById(agent.getId()).get();
        assertEquals(foundAgent, agent);
        verify(agentRepositoryMock, times(2)).findById(agent.getId());
    }

    @Test
    public void findAgents() {
        if (agentRepositoryMock.findAll().size() == 0) {
            when(agentRepositoryMock.findAgentsByText(anyString())).thenReturn(null);
        }

        Agent firstAgent = newAgent();
        Agent secondAgent = newAgent();
        secondAgent.setName(secondAgent.getName() + 's');
        secondAgent.setSurname(secondAgent.getSurname() + 's');
        assertNotEquals(firstAgent, secondAgent);
        assertNotEquals(firstAgent.getSurname(), secondAgent.getSurname());

        when(agentRepositoryMock.findAgentsByText(firstAgent.getName())).thenReturn(Collections.singletonList(firstAgent));
        when(agentRepositoryMock.findAgentsByText(firstAgent.getSurname())).thenReturn(Collections.singletonList(secondAgent));

        secondAgent.setName(firstAgent.getName());
        when(agentRepositoryMock.findAgentsByText(firstAgent.getName())).thenReturn(Arrays.asList(firstAgent, secondAgent));
    }

    @Test
    public void checkAgent() {
        Agent agent = newAgent();
        agentRepositoryMock.save(agent);
        when(agentRepositoryMock.save(any(Agent.class))).thenReturn(new Agent());
        when(agentRepositoryMock.checkAgentByParameters(agent.getName(), agent.getSurname())).thenReturn(agent);
        if (!agentService.checkAgent("Michael","Fox")) {
            when(agentRepositoryMock.checkAgentByParameters("Michael", "Fox")).thenReturn(null);
        }
    }

    private Agent newAgent() {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        return new Agent(""+(Math.random()*10000)+"", "Sam", "Samson", "+1234567890",
                "Address", "Male", now,"");
    }
}
