package com.exadel.guestregistration.services;

import com.exadel.guestregistration.exceptions.EmployeeException;
import com.exadel.guestregistration.exceptions.NotFoundException;
import com.exadel.guestregistration.models.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> findAllEmployees();
    List<Employee> findAllInSpecifOffice(String id);
    Employee create(Employee employee) throws EmployeeException;
    Employee update(Employee employee) throws NotFoundException, EmployeeException;
    void delete(String id) throws NotFoundException;
    Employee findEmployeeById(String id) throws NotFoundException;
    List<Employee> findEmployees(String officeId, String text);
    boolean checkEmployee(String agentId);
    List<Employee>findEmployeesOfFullList(String text);
    List<Employee>findEmployeesByCardId(String cardId);   
}
