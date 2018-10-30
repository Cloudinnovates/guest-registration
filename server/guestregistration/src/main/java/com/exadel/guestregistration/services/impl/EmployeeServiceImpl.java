package com.exadel.guestregistration.services.impl;

import com.exadel.guestregistration.exceptions.EmployeeException;
import com.exadel.guestregistration.exceptions.NotFoundException;
import com.exadel.guestregistration.models.Card;
import com.exadel.guestregistration.models.Employee;
import com.exadel.guestregistration.repositories.AgentRepository;
import com.exadel.guestregistration.repositories.CardRepository;
import com.exadel.guestregistration.repositories.EmployeeRepository;
import com.exadel.guestregistration.repositories.OfficeRepository;
import com.exadel.guestregistration.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private OfficeRepository officeRepository;

    @Override
    public List<Employee> findAllEmployees(){
        return !employeeRepository.findAll().isEmpty()? employeeRepository.findAll(): null;
    }

    @Override
    public List<Employee> findAllInSpecifOffice(String id) {

        if (officeRepository.findById(id).isPresent() && (employeeRepository.findAll().size() != 0)) {
            ArrayList<Employee> employees = new ArrayList<>(employeeRepository.findAll());

            employees.removeIf(employee -> !employee.getOfficeId().equals(id));

            employees.trimToSize();

            return employees;
        } else {
            return null;
        }

    }

    @Override
    public Employee create(Employee employee) throws EmployeeException {
        if (!checkEmployee(employee.getAgentId())) {
            return employeeRepository.insert(employee);
        } else {
            throw new EmployeeException("The employee with the same credentials already exist");
        }
    }

    @Override
    public Employee update(Employee employee) throws EmployeeException, NotFoundException {
        if (employeeRepository.findById(employee.getId()).isPresent()) {

            Employee employeeData = employeeRepository.findById(employee.getId()).get();
            employeeData.setAgentId(employee.getAgentId());
            employeeData.setPosition(employee.getPosition());
            employeeData.setEmployment_date(employee.getEmployment_date());
            employeeData.setCard_issue_date(employee.getCard_issue_date());
            employeeData.setDismissal_date(employee.getDismissal_date());

            Optional<Card> card = cardRepository.findById(employee.getCardId());

            if (card.isPresent() && officeRepository.findById(employee.getOfficeId()).isPresent() &&
                    officeRepository.findById(card.get().getOfficeId()).isPresent()) {
                employeeData.setCardId(employee.getCardId());
                employeeData.setOfficeId(employee.getOfficeId());
                return employeeRepository.save(employeeData);
            } else {
                throw new EmployeeException("Selected card does not apply to selected office");
            }

        } else {
            throw new NotFoundException("Employee with the provided id not found!");
        }
    }

    @Override
    public void delete(String id) throws NotFoundException {
        if(employeeRepository.findById(id).isPresent()) {
            employeeRepository.deleteById(id);
        } else {
            throw new NotFoundException("Employee with the provided id not found!");
        }
    }

    @Override
    public Employee findEmployeeById(String id) throws NotFoundException {
        if (employeeRepository.findById(id).isPresent()) {
            return employeeRepository.findById(id).get();
        } else {
            throw new NotFoundException("Employee with the provided id not found!");
        }
    }

    @Override
    public List<Employee> findEmployees(String officeId, String text) {
        List<Employee> allEmployeesInSelectedOffice = findAllInSpecifOffice(officeId);
        List<Employee> employees = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (Employee e : allEmployeesInSelectedOffice) {

            if (text.matches("[0-9]+") &&
                    (cardRepository.findById(e.getCardId()).get().getCardNo() == Integer.parseInt(text))) {
                employees.add(e);
            } else {

                if (agentRepository.findById(e.getAgentId()).get().getName().toLowerCase().contains(text.toLowerCase()) ||
                        agentRepository.findById(e.getAgentId()).get().getSurname().toLowerCase().contains(text.toLowerCase()) ||
                        agentRepository.findById(e.getAgentId()).get().getPhone().contains(text)) {
                    employees.add(e);
                } else {

                    if (e.getPosition().toLowerCase().contains(text.toLowerCase())) {
                        employees.add(e);
                    } else {

                        if (dateFormat.format(e.getEmployment_date()).contains(text) ||
                                dateFormat.format(e.getCard_issue_date()).contains(text) ||
                                dateFormat.format(e.getDismissal_date()).contains(text)) {
                            employees.add(e);
                        }
                    }
                }
            }
        }

        return employees.isEmpty() ? null: employees;
    }

    @Override
    public List<Employee> findEmployeesOfFullList(String text) {

        List<Employee> employees = new ArrayList<>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (Employee e : findAllEmployees()) {

            if (text.matches("[0-9]+") &&
                    (cardRepository.findById(e.getCardId()).get().getCardNo() == Integer.parseInt(text))) {
                employees.add(e);
            } else {

                if (agentRepository.findById(e.getAgentId()).get().getName().toLowerCase().contains(text.toLowerCase()) ||
                        agentRepository.findById(e.getAgentId()).get().getSurname().toLowerCase().contains(text.toLowerCase()) ||
                        agentRepository.findById(e.getAgentId()).get().getPhone().contains(text)) {
                    employees.add(e);
                } else {

                    if (e.getPosition().toLowerCase().contains(text.toLowerCase()) ||
                            officeRepository.findById(e.getOfficeId()).get().getName()
                                    .toLowerCase().contains(text.toLowerCase())) {
                        employees.add(e);
                    } else {

                        if (dateFormat.format(e.getEmployment_date()).contains(text) ||
                                dateFormat.format(e.getCard_issue_date()).contains(text) ||
                                dateFormat.format(e.getDismissal_date()).contains(text)) {
                            employees.add(e);
                        }
                    }
                }
            }
        }

        return employees.isEmpty() ? null: employees;
    }

    @Override
    public boolean checkEmployee(String agentId) {
        if (employeeRepository.findAll().size() != 0) {
            Employee employee = employeeRepository.checkEmployeeByParameter(agentId);
            return employee != null;
        } else {
            return false;
        }
    }
    
    
    @Override
    public List<Employee> findEmployeesByCardId(String cardId) {
        
    	return employeeRepository.findEmployeeByCardId(cardId);    
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
