package com.exadel.guestregistration.services;

import com.exadel.guestregistration.exceptions.AgentException;
import com.exadel.guestregistration.exceptions.EmployeeException;
import com.exadel.guestregistration.exceptions.NotFoundException;
import com.exadel.guestregistration.models.*;
import com.exadel.guestregistration.repositories.AgentRepository;
import com.exadel.guestregistration.repositories.CardRepository;
import com.exadel.guestregistration.repositories.EmployeeRepository;
import com.exadel.guestregistration.repositories.OfficeRepository;
import com.exadel.guestregistration.services.impl.EmployeeServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EmployeeServiceMockitoTest {

    @Mock
    private EmployeeRepository employeeRepositoryMock;

    @Mock
    private CardRepository cardRepositoryMock;

    @Mock
    private OfficeRepository officeRepositoryMock;

    @Mock
    private AgentRepository agentRepositoryMock;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Captor
    private ArgumentCaptor<Employee> employeeArgumentCaptor;

    @Spy
    EmployeeService employeeServiceSpy;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        employeeArgumentCaptor = ArgumentCaptor.forClass(Employee.class);
    }

    @Test
    public void findAllEmployees() {
        if(employeeRepositoryMock.findAll().size() == 0) {
            when(employeeRepositoryMock.findAll()).thenReturn(null);
        }

        Employee firstEmployee = newEmployee();
        Employee secondEmployee = newEmployee();
        assertNotEquals(firstEmployee, secondEmployee);

        when(employeeRepositoryMock.findAll()).thenReturn(Arrays.asList(firstEmployee, secondEmployee));

        List<Employee> employees = employeeService.findAllEmployees();
        assertFalse(employees.isEmpty());
        assertEquals(2, employees.size());

        verify(employeeRepositoryMock, times(3)).findAll();
        verifyNoMoreInteractions(employeeRepositoryMock);
    }

    @Test
    public void findAllInSpecifOffice() {
        if(employeeRepositoryMock.findAll().size() == 0) {
            when(employeeRepositoryMock.findAll()).thenReturn(null);
        }

        Employee firstEmployee = newEmployee();
        Employee secondEmployee = newEmployee();

        String officeId = firstEmployee.getOfficeId();
        if (officeId.equals(firstEmployee.getOfficeId())) {
            when(employeeServiceSpy.findAllInSpecifOffice(firstEmployee.getOfficeId()))
                    .thenReturn(Collections.singletonList(firstEmployee));
        } else {
            when(employeeServiceSpy.findAllInSpecifOffice(secondEmployee.getOfficeId()))
                    .thenReturn(Collections.singletonList(secondEmployee));
        }

        List <Employee> employees = employeeServiceSpy.findAllInSpecifOffice(officeId);
        assertNotNull(employees);
        assertFalse(employees.isEmpty());
        assertEquals(1, employees.size());
        assertNotEquals(firstEmployee.getOfficeId(), secondEmployee.getOfficeId());


        secondEmployee.setOfficeId(firstEmployee.getOfficeId());
        if (firstEmployee.getOfficeId().equals(secondEmployee.getOfficeId())) {
            when(employeeServiceSpy.findAllInSpecifOffice(officeId))
                    .thenReturn(Arrays.asList(firstEmployee, secondEmployee));
        }

        employees = employeeServiceSpy.findAllInSpecifOffice(officeId);
        assertFalse(employees.isEmpty());
        assertEquals(2, employees.size());
        assertNotEquals(firstEmployee.getId(), secondEmployee.getId());
        assertEquals(firstEmployee.getOfficeId(), secondEmployee.getOfficeId());
    }

    @Test
    public void create() {
        Employee employee = newEmployee();
        if (!employeeService.checkEmployee(employee.getAgentId())) {
            employeeRepositoryMock.save(employee);
            when(employeeRepositoryMock.save(any(Employee.class))).thenReturn(new Employee());
        } else {
            when(employeeRepositoryMock.save(employee)).thenThrow(new AgentException("The agent with the same credentials already exist"));
        }

        verify(employeeRepositoryMock, times(1)).save(any(Employee.class));
        assertThat(employeeRepositoryMock.save(employee), is(notNullValue()));
        verify(employeeRepositoryMock, never()).delete(any(Employee.class));

        verify(employeeRepositoryMock, times(2)).save(employeeArgumentCaptor.capture());

        assertNotNull(employeeArgumentCaptor.getValue());
        assertNotNull(employeeArgumentCaptor.getValue().getId());
        assertEquals(employee.getAgentId(), employeeArgumentCaptor.getValue().getAgentId());
        assertEquals(employee.getCardId(), employeeArgumentCaptor.getValue().getCardId());
        assertEquals(employee.getOfficeId(), employeeArgumentCaptor.getValue().getOfficeId());
        assertEquals(employee.getPosition(), employeeArgumentCaptor.getValue().getPosition());
        assertEquals(employee.getEmployment_date(), employeeArgumentCaptor.getValue().getEmployment_date());
        assertEquals(employee.getCard_issue_date(), employeeArgumentCaptor.getValue().getCard_issue_date());
        assertEquals(employee.getDismissal_date(), employeeArgumentCaptor.getValue().getDismissal_date());
    }

    @Test
    public void update() throws EmployeeException, NotFoundException {
        Employee employee = newEmployee();
        employeeRepositoryMock.save(employee);
        when(employeeRepositoryMock.save(any(Employee.class))).thenReturn(new Employee());
        when(employeeRepositoryMock.findById(employee.getId())).thenReturn(java.util.Optional.of(employee));
        assertTrue(employeeRepositoryMock.findById(employee.getId()).isPresent());

        Employee foundEmployee = employeeRepositoryMock.findById(employee.getId()).get();
        assertNotNull(foundEmployee);
        assertEquals(employee, foundEmployee);

        //update of some employee parameters
        String agentId = foundEmployee.getAgentId() + "123";
        foundEmployee.setAgentId(foundEmployee.getAgentId() + "123");
        foundEmployee.setPosition("Administrator");
        String officeId = foundEmployee.getOfficeId() + "123";
        foundEmployee.setOfficeId(foundEmployee.getOfficeId() + "123");

        //check that card is related to selected office
        when(cardRepositoryMock.findById(employee.getCardId())).thenReturn(Optional.of(new Card()));
        assertTrue(cardRepositoryMock.findById(foundEmployee.getCardId()).isPresent());
        Card card = cardRepositoryMock.findById(foundEmployee.getCardId()).get();
        when(officeRepositoryMock.findById(employee.getOfficeId())).thenReturn(Optional.of(new Office()));
        when(officeRepositoryMock.findById(card.getOfficeId())).thenReturn(Optional.of(new Office()));

        //check of employee updated information (changes that performed above)
        if (!employeeService.checkEmployee(foundEmployee.getAgentId())) {
            employeeRepositoryMock.save(foundEmployee);
            when(employeeService.update(foundEmployee)).thenReturn(new Employee());

            when(employeeRepositoryMock.findById(foundEmployee.getId())).thenReturn(java.util.Optional.of(foundEmployee));
            assertEquals(foundEmployee.getAgentId(), agentId);
            assertEquals(foundEmployee.getPosition(), "Administrator");
            assertEquals(foundEmployee.getOfficeId(), officeId);
        } else {
            when(employeeService.update(foundEmployee)).thenThrow(new AgentException("The agent with the same credentials already exist"));
        }

        verify(employeeRepositoryMock, times(2)).save(any(Employee.class));
    }

    @Test
    public void delete() throws NotFoundException {
        Employee employee = newEmployee();
        when(employeeRepositoryMock.findById(employee.getId())).thenReturn(java.util.Optional.of(employee));
        assertNotNull(employeeRepositoryMock.findById(employee.getId()));

        doNothing().when(employeeRepositoryMock).deleteById(employee.getId());
        employeeService.delete(employee.getId());

        verify(employeeRepositoryMock, times(1)).deleteById(employee.getId());
    }

    @Test
    public void findEmployeeById() {
        if (employeeRepositoryMock.findAll().size() == 0) {
            when(employeeRepositoryMock.findById(anyString())).thenReturn(null);
        }

        Employee employee = newEmployee();
        when(employeeRepositoryMock.findById(employee.getId())).thenReturn(java.util.Optional.of(employee));
        assertTrue(employeeRepositoryMock.findById(employee.getId()).isPresent());

        Employee foundEmployee = employeeRepositoryMock.findById(employee.getId()).get();
        assertEquals(foundEmployee, employee);
        verify(employeeRepositoryMock, times(2)).findById(employee.getId());
    }

    @Test
    public void findEmployees() {
        if (employeeRepositoryMock.findAll().size() == 0) {
            when(employeeRepositoryMock.findAll()).thenReturn(null);
        }

        Employee firstEmployee = newEmployee();
        Employee secondEmployee = newEmployee();
        when(employeeRepositoryMock.save(firstEmployee)).thenReturn(new Employee());
        when(employeeRepositoryMock.save(secondEmployee)).thenReturn(new Employee());

        assertNotEquals(firstEmployee, secondEmployee);
        assertNotEquals(firstEmployee.getAgentId(), secondEmployee.getAgentId());
        assertNotEquals(firstEmployee.getCardId(), secondEmployee.getCardId());

        Agent firstAgent = newAgent("Felix");
        Agent secondAgent = newAgent("John");
        when(agentRepositoryMock.findById(firstAgent.getId())).thenReturn(Optional.of(firstAgent));
        when(agentRepositoryMock.findById(secondAgent.getId())).thenReturn(Optional.of(secondAgent));

        firstEmployee.setAgentId(firstAgent.getId());
        secondEmployee.setAgentId(secondAgent.getId());
        employeeRepositoryMock.save(firstEmployee);
        employeeRepositoryMock.save(secondEmployee);

        assertTrue(agentRepositoryMock.findById(firstEmployee.getAgentId()).isPresent());
        assertTrue(agentRepositoryMock.findById(secondEmployee.getAgentId()).isPresent());

        when(employeeRepositoryMock.findAll()).thenReturn(Arrays.asList(firstEmployee, secondEmployee));

        //checking results by 'Name' parameter
        when(employeeServiceSpy.findEmployees(firstEmployee.getOfficeId(), firstAgent.getName().substring(0,3)))
                .thenReturn(Collections.singletonList(firstEmployee));

        List <Employee> employees = employeeServiceSpy.findEmployees(firstEmployee.getOfficeId(), firstAgent.getName().substring(0,3));
        assertFalse(employees.isEmpty());
        assertEquals(1, employees.size());

        //checking results by 'Surname' parameter
        firstEmployee.setOfficeId(secondEmployee.getOfficeId());
        when(employeeServiceSpy.findEmployees(firstEmployee.getOfficeId(), firstAgent.getSurname()))
                .thenReturn(Arrays.asList(firstEmployee, secondEmployee));

        employees = employeeServiceSpy.findEmployees(firstEmployee.getOfficeId(), firstAgent.getSurname());
        assertFalse(employees.isEmpty());
        assertEquals(2, employees.size());
        assertNotEquals(firstEmployee.getAgentId(), secondEmployee.getAgentId());
        assertEquals(agentRepositoryMock.findById(firstEmployee.getAgentId()).get().getSurname(),
                agentRepositoryMock.findById(secondEmployee.getAgentId()).get().getSurname());

        //checking results by 'Phone' parameter
        when(employeeServiceSpy.findEmployees(secondEmployee.getOfficeId(), secondAgent.getPhone().substring(0,4)))
                .thenReturn(Arrays.asList(firstEmployee, secondEmployee));

        employees = employeeServiceSpy.findEmployees(secondEmployee.getOfficeId(), secondAgent.getPhone().substring(0,4));
        assertFalse(employees.isEmpty());
        assertEquals(2, employees.size());
        assertEquals(agentRepositoryMock.findById(firstEmployee.getAgentId()).get().getPhone(),
                agentRepositoryMock.findById(secondEmployee.getAgentId()).get().getPhone());

        //checking results by 'Card No.' parameter
        Card card = newCard(firstEmployee.getCardId(), firstEmployee.getOfficeId());
        firstEmployee.setCardId(card.getId());

        when(cardRepositoryMock.findById(card.getId())).thenReturn(Optional.of(card));
        when(employeeServiceSpy.findEmployees(firstEmployee.getOfficeId(), Integer.toString(card.getCardNo())))
                .thenReturn(Collections.singletonList(firstEmployee));

        employees = employeeServiceSpy.findEmployees(firstEmployee.getOfficeId(), Integer.toString(card.getCardNo()));
        assertFalse(employees.isEmpty());
        assertEquals(1 ,employees.size());

        //checking results by 'Position' parameter
        secondEmployee.setPosition("System Administrator");

        when(employeeServiceSpy.findEmployees(secondEmployee.getOfficeId(), "Administrator"))
                .thenReturn(Collections.singletonList(secondEmployee));

        employees = employeeServiceSpy.findEmployees(secondEmployee.getOfficeId(), "Administrator");
        assertFalse(employees.isEmpty());
        assertEquals(1, employees.size());


        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        //checking results by 'Employment Date' parameter
        when(employeeServiceSpy.findEmployees(firstEmployee.getOfficeId(),
                dateFormat.format(firstEmployee.getEmployment_date()))).
                thenReturn(Arrays.asList(firstEmployee, secondEmployee));

        employees = employeeServiceSpy.findEmployees(firstEmployee.getOfficeId(),
                dateFormat.format(firstEmployee.getEmployment_date()));
        assertFalse(employees.isEmpty());
        assertEquals(2, employees.size());
        assertNotEquals(firstEmployee.getAgentId(), secondEmployee.getAgentId());

        //checking results by 'Card Issue Date' parameter
        when(employeeServiceSpy.findEmployees(firstEmployee.getOfficeId(),
                dateFormat.format(secondEmployee.getCard_issue_date()).substring(0,6))).
                thenReturn(Arrays.asList(firstEmployee, secondEmployee));

        employees = employeeServiceSpy.findEmployees(firstEmployee.getOfficeId(),
                dateFormat.format(secondEmployee.getCard_issue_date()).substring(0,6));
        assertFalse(employees.isEmpty());
        assertEquals(2, employees.size());
        assertNotEquals(firstEmployee.getAgentId(), secondEmployee.getAgentId());

        //checking results by 'Dismissal Date' parameter
        secondEmployee.setDismissal_date(Calendar.getInstance().getTime());
        assertNotEquals(firstEmployee.getDismissal_date(), secondEmployee.getDismissal_date());

        when(employeeServiceSpy.findEmployees(secondEmployee.getOfficeId(),
                dateFormat.format(secondEmployee.getDismissal_date()))).
                thenReturn(Collections.singletonList(secondEmployee));

        employees = employeeServiceSpy.findEmployees(secondEmployee.getOfficeId(),
                dateFormat.format(secondEmployee.getDismissal_date()));
        assertFalse(employees.isEmpty());
        assertEquals(1, employees.size());
        assertEquals(employees.get(0).getDismissal_date(), secondEmployee.getDismissal_date());
    }

    @Test
    public void checkEmployee() {
        Employee employee = newEmployee();
        employeeRepositoryMock.save(employee);
        when(employeeRepositoryMock.save(any(Employee.class))).thenReturn(new Employee());
        when(employeeRepositoryMock.checkEmployeeByParameter(employee.getAgentId())).thenReturn(employee);
        if (!employeeService.checkEmployee(employee.getAgentId() + "123")) {
            when(employeeRepositoryMock.checkEmployeeByParameter(employee.getAgentId() + "123")).thenReturn(null);
        }
    }

    @Test
    public void findEmployeesOfFullList() {
        if (employeeRepositoryMock.findAll().size() == 0) {
            when(employeeRepositoryMock.findAll()).thenReturn(null);
        }

        Employee firstEmployee = newEmployee();
        Employee secondEmployee = newEmployee();
        when(employeeRepositoryMock.save(firstEmployee)).thenReturn(new Employee());
        when(employeeRepositoryMock.save(secondEmployee)).thenReturn(new Employee());

        assertNotEquals(firstEmployee, secondEmployee);
        assertNotEquals(firstEmployee.getAgentId(), secondEmployee.getAgentId());
        assertNotEquals(firstEmployee.getCardId(), secondEmployee.getCardId());

        Agent firstAgent = newAgent("Sam");
        Agent secondAgent = newAgent("John");
        when(agentRepositoryMock.findById(firstAgent.getId())).thenReturn(Optional.of(firstAgent));
        when(agentRepositoryMock.findById(secondAgent.getId())).thenReturn(Optional.of(secondAgent));

        firstEmployee.setAgentId(firstAgent.getId());
        secondEmployee.setAgentId(secondAgent.getId());
        employeeRepositoryMock.save(firstEmployee);
        employeeRepositoryMock.save(secondEmployee);

        assertTrue(agentRepositoryMock.findById(firstEmployee.getAgentId()).isPresent());
        assertTrue(agentRepositoryMock.findById(secondEmployee.getAgentId()).isPresent());

        when(employeeRepositoryMock.findAll()).thenReturn(Arrays.asList(firstEmployee, secondEmployee));

        //checking results by 'Name' parameter
        when(employeeServiceSpy.findEmployeesOfFullList(firstAgent.getName()))
                .thenReturn(Collections.singletonList(firstEmployee));

        List <Employee> employees = employeeServiceSpy.findEmployeesOfFullList(firstAgent.getName());
        assertFalse(employees.isEmpty());
        assertEquals(1, employees.size());

        //checking results by 'Surname' parameter
        when(employeeServiceSpy.findEmployeesOfFullList(firstAgent.getSurname()))
                .thenReturn(Arrays.asList(firstEmployee, secondEmployee));

        employees = employeeServiceSpy.findEmployeesOfFullList(firstAgent.getSurname());
        assertFalse(employees.isEmpty());
        assertEquals(2, employees.size());
        assertNotEquals(firstEmployee.getAgentId(), secondEmployee.getAgentId());
        assertEquals(agentRepositoryMock.findById(firstEmployee.getAgentId()).get().getSurname(),
                agentRepositoryMock.findById(secondEmployee.getAgentId()).get().getSurname());

        //checking results by 'Phone' parameter
        when(employeeServiceSpy.findEmployeesOfFullList(secondAgent.getPhone()))
                .thenReturn(Arrays.asList(firstEmployee, secondEmployee));

        employees = employeeServiceSpy.findEmployeesOfFullList(secondAgent.getPhone());
        assertFalse(employees.isEmpty());
        assertEquals(2, employees.size());
        assertEquals(agentRepositoryMock.findById(firstEmployee.getAgentId()).get().getPhone(),
                agentRepositoryMock.findById(secondEmployee.getAgentId()).get().getPhone());

        //checking results by 'Card No.' parameter
        Card card = newCard(firstEmployee.getCardId(), firstEmployee.getOfficeId());
        firstEmployee.setCardId(card.getId());
        when(cardRepositoryMock.findById(card.getId())).thenReturn(Optional.of(card));
        when(employeeServiceSpy.findEmployeesOfFullList(Integer.toString(card.getCardNo())))
                .thenReturn(Collections.singletonList(firstEmployee));

        employees = employeeServiceSpy.findEmployeesOfFullList(Integer.toString(card.getCardNo()));
        assertFalse(employees.isEmpty());
        assertEquals(1 ,employees.size());

        //checking results by 'Position' parameter
        when(employeeServiceSpy.findEmployeesOfFullList(secondEmployee.getPosition()))
                .thenReturn(Arrays.asList(firstEmployee, secondEmployee));

        employees = employeeServiceSpy.findEmployeesOfFullList(firstEmployee.getPosition());
        assertFalse(employees.isEmpty());
        assertEquals(2, employees.size());
        assertNotEquals(firstEmployee.getAgentId(), secondEmployee.getAgentId());

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        //checking results by 'Employment Date' parameter
        when(employeeServiceSpy.findEmployeesOfFullList(dateFormat.format(firstEmployee.getEmployment_date())))
                .thenReturn(Arrays.asList(firstEmployee, secondEmployee));

        employees = employeeServiceSpy.findEmployeesOfFullList(dateFormat.format(firstEmployee.getEmployment_date()));
        assertFalse(employees.isEmpty());
        assertEquals(2, employees.size());
        assertNotEquals(firstEmployee.getAgentId(), secondEmployee.getAgentId());

        //checking results by 'Card Issue Date' parameter
        when(employeeServiceSpy.findEmployeesOfFullList(dateFormat.format(secondEmployee.getCard_issue_date())))
                .thenReturn(Arrays.asList(firstEmployee, secondEmployee));

        employees = employeeServiceSpy.findEmployeesOfFullList(dateFormat.format(secondEmployee.getCard_issue_date()));
        assertFalse(employees.isEmpty());
        assertEquals(2, employees.size());
        assertNotEquals(firstEmployee.getAgentId(), secondEmployee.getAgentId());

        //checking results by 'Dismissal Date' parameter
        secondEmployee.setDismissal_date(Calendar.getInstance().getTime());
        assertNotEquals(firstEmployee.getDismissal_date(), secondEmployee.getDismissal_date());

        when(employeeServiceSpy.findEmployeesOfFullList(dateFormat.format(secondEmployee.getDismissal_date())))
                .thenReturn(Collections.singletonList(secondEmployee));

        employees = employeeServiceSpy.findEmployeesOfFullList(dateFormat.format(secondEmployee.getDismissal_date()));
        assertFalse(employees.isEmpty());
        assertEquals(1, employees.size());
        assertEquals(employees.get(0).getDismissal_date(), secondEmployee.getDismissal_date());

        //checking results by 'Location' parameter. Option No.1
        Office office = newOffice(firstEmployee.getOfficeId());
        when(employeeServiceSpy.findEmployeesOfFullList(office.getName()))
                .thenReturn(Collections.singletonList(firstEmployee));

        employees = employeeServiceSpy.findEmployeesOfFullList(office.getName());
        assertFalse(employees.isEmpty());
        assertEquals(1, employees.size());
        assertEquals(employees.get(0).getId(), firstEmployee.getId());

        //checking results by 'Location' parameter. Option No.2
        secondEmployee.setOfficeId(office.getId());
        when(employeeServiceSpy.findEmployeesOfFullList(office.getName()))
                .thenReturn(Arrays.asList(firstEmployee, secondEmployee));

        employees = employeeServiceSpy.findEmployeesOfFullList(office.getName());
        assertFalse(employees.isEmpty());
        assertEquals(2, employees.size());
        assertNotEquals(employees.get(0).getId(), employees.get(1).getId());
        assertEquals(employees.get(0).getOfficeId(), employees.get(1).getOfficeId());
    }

    private Employee newEmployee() {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        return new Employee(""+(Math.random()*10000)+"", ""+(Math.random()*10000)+"", "Developer", now,
                now, now, ""+(Math.random()*10000)+"", ""+(Math.random()*10000)+"");
    }

    private Agent newAgent(String name) {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        return new Agent(""+(Math.random()*10000)+"", name, "Samson", "+1234567890",
                "Address", "Male", now,"");
    }

    private Card newCard(String id, String officeId) {
        return new Card(id, (int)(Math.random()*1000), new Date(1537522766078L), new Date(1593162000000L),
                officeId, "Full-time Employee", true);
    }

    private Office newOffice(String id) {
        return new Office(id, "Paris", "Address 1",
                "Phone No. 1", "Some email", "Some type",
                "Name", "Surname", "Email");
    }
}
