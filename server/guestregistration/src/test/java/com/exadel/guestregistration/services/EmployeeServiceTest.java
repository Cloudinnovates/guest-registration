package com.exadel.guestregistration.services;

import com.exadel.guestregistration.exceptions.NotFoundException;
import com.exadel.guestregistration.models.*;
import com.exadel.guestregistration.repositories.AgentRepository;
import com.exadel.guestregistration.repositories.CardRepository;
import com.exadel.guestregistration.repositories.EmployeeRepository;
import com.exadel.guestregistration.repositories.OfficeRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeServiceTest {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    private OfficeRepository officeRepository;
    @Autowired
    private CardRepository cardRepository;

    @Before
    public void setup() {
        Assert.assertNotNull(employeeRepository);
        Assert.assertNotNull(employeeService);
        Assert.assertNotNull(agentRepository);
        Assert.assertNotNull(officeRepository);
        Assert.assertNotNull(cardRepository);
    }

    @Test
    public void findAllEmployees() {
        Employee employee = newEmployee();
        employeeRepository.save(employee);
        List<Employee> employees = employeeRepository.findAll();

        Assert.assertNotNull(employees);
        Assert.assertEquals(employees.size(), employeeRepository.findAll().size());

        Employee anotherEmployee = newEmployee();
        employeeRepository.save(anotherEmployee);
        Assert.assertEquals(employees.size() + 1, employeeRepository.findAll().size());

        employees = employeeRepository.findAll();
        Assert.assertNotSame(employees.get(0), employees.get(1));

        employeeRepository.deleteById(employee.getId());
        employeeRepository.deleteById(anotherEmployee.getId());
    }

    @Test
    public void findAllInSpecifOffice() {
        Employee firstEmployee = newEmployee();
        Employee secondEmployee = newEmployee();
        Employee thirdEmployee = newEmployee();
        thirdEmployee.setOfficeId(firstEmployee.getOfficeId());
        employeeRepository.save(firstEmployee);
        employeeRepository.save(secondEmployee);
        employeeRepository.save(thirdEmployee);
        officeRepository.save(newOffice(firstEmployee.getOfficeId()));
        officeRepository.save(newOffice(secondEmployee.getOfficeId()));


        List <Employee> employees = employeeService.findAllInSpecifOffice(firstEmployee.getOfficeId());
        Assert.assertFalse(employees.isEmpty());
        Assert.assertTrue(employees.size() > 1);

        Assert.assertNotNull(employees.get(0));
        Assert.assertNotNull(employees.get(1));
        Assert.assertEquals(employees.get(0).getOfficeId(), employees.get(1).getOfficeId());
        Assert.assertNotEquals(employees.get(0).getId(), employees.get(1).getId());

        officeRepository.deleteById(firstEmployee.getOfficeId());
        officeRepository.deleteById(secondEmployee.getOfficeId());
        employeeRepository.deleteById(firstEmployee.getId());
        employeeRepository.deleteById(secondEmployee.getId());
        employeeRepository.deleteById(thirdEmployee.getId());
    }

    @Test
    public void create() throws NotFoundException {
        Employee employee = newEmployee();
        Assert.assertFalse(employeeService.checkEmployee(employee.getAgentId()));
        employeeRepository.save(employee);
        Assert.assertTrue(employeeService.checkEmployee(employee.getAgentId()));

        Employee foundEmployee = employeeService.findEmployeeById(employee.getId());

        Assert.assertNotNull(foundEmployee);

        Assert.assertNotNull(foundEmployee.getOfficeId());
        Assert.assertNotNull(foundEmployee.getCardId());
        Assert.assertNotNull(foundEmployee.getAgentId());
        Assert.assertNotNull(foundEmployee.getDismissal_date());
        Assert.assertNotNull(foundEmployee.getPosition());
        Assert.assertNotNull(foundEmployee.getEmployment_date());
        Assert.assertNotNull(foundEmployee.getCard_issue_date());

        Assert.assertEquals(employee.getOfficeId(), foundEmployee.getOfficeId());
        Assert.assertEquals(employee.getCardId(), foundEmployee.getCardId());
        Assert.assertEquals(employee.getAgentId(), foundEmployee.getAgentId());
        Assert.assertEquals(employee.getDismissal_date(), foundEmployee.getDismissal_date());
        Assert.assertEquals(employee.getPosition(), foundEmployee.getPosition());
        Assert.assertEquals(employee.getEmployment_date(), foundEmployee.getEmployment_date());
        Assert.assertEquals(employee.getCard_issue_date(), foundEmployee.getCard_issue_date());

        employeeRepository.deleteById(employee.getId());
    }

    @Test
    public void update() {
        Employee firstEmployee = newEmployee();
        Employee secondEmployee = newEmployee();
        Assert.assertNotEquals(firstEmployee.getId(), secondEmployee.getId());

        secondEmployee.setId(firstEmployee.getId());
        Assert.assertEquals(firstEmployee.getId(), secondEmployee.getId());

        secondEmployee.setId("1234567890");
        employeeRepository.save(secondEmployee);

        Assert.assertTrue(employeeRepository.findById(secondEmployee.getId()).isPresent());

        secondEmployee.setPosition("Administrator");
        Assert.assertNotEquals(firstEmployee.getPosition(), secondEmployee.getPosition());

        secondEmployee.setAgentId(newAgent("0909090909").getId());
        Assert.assertFalse(employeeService.checkEmployee(secondEmployee.getAgentId()));

        String office = secondEmployee.getOfficeId();
        secondEmployee.setOfficeId(newOffice("12345").getId());
        Assert.assertNotEquals(office, secondEmployee.getOfficeId());

        agentRepository.deleteById(secondEmployee.getId());
    }

    @Test
    public void delete() {
        Employee employee = newEmployee();
        employeeRepository.save(employee);
        Assert.assertTrue(employeeRepository.findById(employee.getId()).isPresent());
        Assert.assertNotNull(employeeRepository.findById(employee.getId()).get());
        employeeRepository.deleteById(employee.getId());
        Assert.assertFalse(employeeRepository.findById(employee.getId()).isPresent());
    }

    @Test
    public void findEmployeeById() throws NotFoundException {
        Employee employee = newEmployee();
        Assert.assertFalse(employeeService.checkEmployee(employee.getAgentId()));
        employeeRepository.save(employee);

        Employee foundEmployee = employeeService.findEmployeeById(employee.getId());
        Assert.assertNotNull(foundEmployee);

        Assert.assertNotNull(foundEmployee.getAgentId());
        Assert.assertEquals(foundEmployee.getAgentId(), employee.getAgentId());
        Assert.assertNotNull(foundEmployee.getOfficeId());
        Assert.assertEquals(foundEmployee.getOfficeId(), employee.getOfficeId());
        Assert.assertNotNull(foundEmployee.getCardId());
        Assert.assertEquals(foundEmployee.getCardId(), employee.getCardId());
        Assert.assertEquals(foundEmployee.getEmployment_date(), employee.getEmployment_date());
        Assert.assertEquals(foundEmployee.getCard_issue_date(), employee.getCard_issue_date());
        Assert.assertEquals(foundEmployee.getDismissal_date(), employee.getDismissal_date());
        Assert.assertEquals(foundEmployee.getPosition(), employee.getPosition());

        employeeRepository.deleteById(employee.getId());
    }

    @Test
    public void findEmployees() {
        Employee firstEmployee = newEmployee();
        Employee secondEmployee = newEmployee();
        Employee thirdEmployee = newEmployee();

        Agent firstAgent = newAgent(firstEmployee.getAgentId());
        Agent secondAgent = newAgent(secondEmployee.getAgentId());
        Agent thirdAgent = newAgent(thirdEmployee.getAgentId());
        thirdEmployee.setOfficeId(firstEmployee.getOfficeId());
        Card card = newCard(firstEmployee.getCardId(), firstEmployee.getOfficeId());

        officeRepository.save(newOffice(firstEmployee.getOfficeId()));
        officeRepository.save(newOffice(secondEmployee.getOfficeId()));
        cardRepository.save(card);
        cardRepository.save(newCard(secondEmployee.getCardId(), secondEmployee.getOfficeId()));
        cardRepository.save(newCard(thirdEmployee.getCardId(), thirdEmployee.getOfficeId()));
        agentRepository.save(firstAgent);
        agentRepository.save(secondAgent);
        agentRepository.save(thirdAgent);
        employeeRepository.save(firstEmployee);
        employeeRepository.save(secondEmployee);
        employeeRepository.save(thirdEmployee);


        //employee position
        List <Employee> employees = employeeService.findEmployees(firstEmployee.getOfficeId(), firstEmployee.getPosition());
        Assert.assertFalse(employees.isEmpty());
        Assert.assertTrue(employees.size() > 1);
        Assert.assertNotSame(employees.get(0), employees.get(1));
        Assert.assertEquals(employees.get(0).getPosition(), employees.get(1).getPosition());

        //employee card no.
        employees = employeeService.findEmployees(firstEmployee.getOfficeId(), Integer.toString(card.getCardNo()));
        Assert.assertFalse(employees.isEmpty());
        Assert.assertTrue(employees.size() == 1);
        Assert.assertEquals(employees.get(0).getCardId(), card.getId());

        //agent name
        employees = employeeService.findEmployees(secondEmployee.getOfficeId(), firstAgent.getName().substring(0,2));
        Assert.assertFalse(employees.isEmpty());
        Assert.assertEquals(employees.get(0).getOfficeId(), secondEmployee.getOfficeId());
        Assert.assertNotNull(employees.get(0).getAgentId());

        //agent surname
        employees = employeeService.findEmployees(thirdEmployee.getOfficeId(), secondAgent.getSurname());
        Assert.assertFalse(employees.isEmpty());
        Assert.assertEquals(employees.get(0).getOfficeId(), thirdEmployee.getOfficeId());
        Assert.assertNotNull(employees.get(0).getAgentId());

        //agent phone number
        Assert.assertNotNull(firstAgent.getPhone());
        employees = employeeService.findEmployees(firstEmployee.getOfficeId(), firstAgent.getPhone());
        Assert.assertEquals(employees.get(0).getOfficeId(), firstEmployee.getOfficeId());
        Assert.assertNotNull(employees.get(0).getAgentId());

        //employee card issue date
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        employees = employeeService.findEmployees(firstEmployee.getOfficeId(), dateFormat.format(firstEmployee.getCard_issue_date()));
        Assert.assertFalse(employees.isEmpty());
        Assert.assertEquals(employees.get(0).getOfficeId(), firstEmployee.getOfficeId());

        //employee employment date
        employees = employeeService.findEmployees(secondEmployee.getOfficeId(),
                dateFormat.format(secondEmployee.getEmployment_date()));
        Assert.assertFalse(employees.isEmpty());
        Assert.assertEquals(employees.get(0).getOfficeId(), secondEmployee.getOfficeId());

        //employee dismissal date
        employees = employeeService.findEmployees(thirdEmployee.getOfficeId(), dateFormat.format(thirdEmployee.getDismissal_date()));
        Assert.assertFalse(employees.isEmpty());
        Assert.assertEquals(employees.get(0).getOfficeId(), thirdEmployee.getOfficeId());


        cardRepository.deleteById(card.getId());
        cardRepository.deleteById(secondEmployee.getCardId());
        cardRepository.deleteById(thirdEmployee.getCardId());
        agentRepository.deleteById(thirdAgent.getId());
        agentRepository.deleteById(secondAgent.getId());
        agentRepository.deleteById(firstAgent.getId());
        officeRepository.deleteById(firstEmployee.getOfficeId());
        officeRepository.deleteById(secondEmployee.getOfficeId());
        employeeRepository.deleteById(firstEmployee.getId());
        employeeRepository.deleteById(secondEmployee.getId());
        employeeRepository.deleteById(thirdEmployee.getId());
    }

    @Test
    public void checkEmployee() {
        Employee employee = newEmployee();
        Assert.assertFalse(employeeService.checkEmployee(employee.getAgentId()));
        employeeRepository.save(employee);
        Assert.assertTrue(employeeService.checkEmployee(employee.getAgentId()));

        Employee anotherEmployee = newExistingEmployee(employee.getAgentId());
        Assert.assertTrue(employeeService.checkEmployee(anotherEmployee.getAgentId()));

        Assert.assertEquals(employee.getAgentId(), anotherEmployee.getAgentId());

        anotherEmployee = newEmployee();
        Assert.assertNotEquals(employee.getAgentId(), anotherEmployee.getAgentId());
        Assert.assertFalse(employeeService.checkEmployee(anotherEmployee.getAgentId()));

        employeeRepository.deleteById(employee.getId());
    }

    @Test
    public void findEmployeesOfFullList() {
        Employee firstEmployee = newEmployee();
        Employee secondEmployee = newEmployee();
        Employee thirdEmployee = newEmployee();

        Agent firstAgent = newAgent(firstEmployee.getAgentId());
        Agent secondAgent = newAgent(secondEmployee.getAgentId());
        Agent thirdAgent = newAgent(thirdEmployee.getAgentId());

        Office firstOffice = newOffice(firstEmployee.getOfficeId());
        Office secondOffice = newOffice(secondEmployee.getOfficeId());
        Office thirdOffice = newOffice(thirdEmployee.getOfficeId());
        Card card = newCard(firstEmployee.getCardId(), firstEmployee.getOfficeId());

        cardRepository.save(card);
        cardRepository.save(newCard(secondEmployee.getCardId(), secondEmployee.getOfficeId()));
        cardRepository.save(newCard(thirdEmployee.getCardId(), thirdEmployee.getOfficeId()));
        officeRepository.save(firstOffice);
        officeRepository.save(secondOffice);
        officeRepository.save(thirdOffice);
        agentRepository.save(firstAgent);
        agentRepository.save(secondAgent);
        agentRepository.save(thirdAgent);
        employeeRepository.save(firstEmployee);
        employeeRepository.save(secondEmployee);
        employeeRepository.save(thirdEmployee);


        //employee position
        List<Employee> employees = employeeService.findEmployeesOfFullList(firstEmployee.getPosition());
        Assert.assertFalse(employees.isEmpty());
        Assert.assertNotNull(employees.get(0).getPosition());
        Assert.assertTrue(employees.size() > 2);
        Assert.assertNotSame(employees.get(0), employees.get(1));

        //card no.
        employees = employeeService.findEmployees(firstEmployee.getOfficeId(), Integer.toString(card.getCardNo()));
        Assert.assertFalse(employees.isEmpty());
        Assert.assertTrue(employees.size() == 1);
        Assert.assertEquals(employees.get(0).getCardId(), card.getId());

        //employee position substring
        employees = employeeService.findEmployeesOfFullList(secondEmployee.getPosition().substring(0,3));
        Assert.assertFalse(employees.isEmpty());
        Assert.assertNotSame(employees.get(0), employees.get(1));
        Assert.assertEquals(employees.get(0).getPosition(), employees.get(1).getPosition());
        Assert.assertEquals(employees.get(1).getPosition(), employees.get(2).getPosition());

        //agent name
        employees = employeeService.findEmployeesOfFullList(firstAgent.getName());
        Assert.assertFalse(employees.isEmpty());

        //agent surname
        employees = employeeService.findEmployeesOfFullList(secondAgent.getSurname());
        Assert.assertFalse(employees.isEmpty());

        //agent phone number
        employees = employeeService.findEmployeesOfFullList(thirdAgent.getPhone());
        Assert.assertFalse(employees.isEmpty());

        //office name (location)
        employees = employeeService.findEmployeesOfFullList(firstOffice.getName());
        Assert.assertFalse(employees.isEmpty());

        //employee card issue date
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        employees = employeeService.findEmployeesOfFullList(dateFormat.format(firstEmployee.getCard_issue_date()));
        Assert.assertFalse(employees.isEmpty());

        //employee employment date
        employees = employeeService.findEmployeesOfFullList(dateFormat.format(secondEmployee.getEmployment_date()));
        Assert.assertFalse(employees.isEmpty());

        //employee dismissal date
        employees = employeeService.findEmployeesOfFullList(dateFormat.format(thirdEmployee.getDismissal_date()));
        Assert.assertFalse(employees.isEmpty());


        cardRepository.deleteById(card.getId());
        cardRepository.deleteById(secondEmployee.getCardId());
        cardRepository.deleteById(thirdEmployee.getCardId());
        officeRepository.deleteById(firstOffice.getId());
        officeRepository.deleteById(secondOffice.getId());
        officeRepository.deleteById(thirdOffice.getId());
        agentRepository.deleteById(firstAgent.getId());
        agentRepository.deleteById(secondAgent.getId());
        agentRepository.deleteById(thirdAgent.getId());
        employeeRepository.deleteById(firstEmployee.getId());
        employeeRepository.deleteById(secondEmployee.getId());
        employeeRepository.deleteById(thirdEmployee.getId());
    }

    private Employee newEmployee() {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        return new Employee(""+(Math.random()*10000)+"", ""+(Math.random()*10000)+"", "Developer", now,
                now, now, ""+(Math.random()*10000)+"", ""+(Math.random()*10000)+"");
    }

    private Employee newExistingEmployee(String agentId) {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        return new Employee(""+(Math.random()*10000)+"", agentId, "Developer", now,
                now, now, ""+(Math.random()*10000)+"", ""+(Math.random()*10000)+"");
    }

    private Agent newAgent(String id) {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        return new Agent(id, "Sam", "Samson", "+1234567890",
                "Address", "Male", now,"");
    }

    private Office newOffice(String id) {
        return new Office(id, "Paris", "Address 1",
                "Phone No. 1", "Some email", "Some type",
                "Name", "Surname", "Email");
    }

    private Card newCard(String id, String officeId) {
        return new Card(id, (int)(Math.random()*1000), new Date(1537522766078L), new Date(1593162000000L),
                officeId, CardTypes.getAllTypes().get((int)(Math.random()*CardTypes.getAllTypes().size())),
                true);
    }
}