package com.exadel.guestregistration.controllers;

import com.exadel.guestregistration.exceptions.EmployeeException;
import com.exadel.guestregistration.exceptions.NotFoundException;
import com.exadel.guestregistration.models.Employee;
import com.exadel.guestregistration.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employees/getAll")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok().body(employeeService.findAllEmployees());
    }

    @GetMapping("/employees/getAll/{id}")
    public ResponseEntity<List<Employee>> getAllEmployeesOfSpecificOffice(@PathVariable("id") String id) {
        return ResponseEntity.ok().body(employeeService.findAllInSpecifOffice(id));
    }

    @PostMapping("/employees/create")
    public ResponseEntity<?> createEmployee(@Valid @RequestBody Employee employee) {
        try {
            return ResponseEntity.ok().body(employeeService.create(employee));
        } catch (EmployeeException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Employee with the same credentials already exist");
        }
    }

    @GetMapping(value = "/employees/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable("id") String id) {
        try {
            return ResponseEntity.ok().body(employeeService.findEmployeeById(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee was not found");
        }
    }

    @PutMapping(value = "/employees/update/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable("id") String id, @Valid @RequestBody Employee employee) {
        try {
            return ResponseEntity.ok().body(employeeService.update(employee));
        } catch (EmployeeException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Employee with the same credentials already exist");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee was not found");
        }
    }

    @DeleteMapping(value = "/employees/delete/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") String id) {
        try {
            employeeService.delete(id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee was not found");
        }
    }

    @GetMapping("/employees/find/{officeId}/{text}")
    public List<Employee> getEmployees(@PathVariable("officeId") String officeId, @PathVariable("text") String text) {
        return employeeService.findEmployees(officeId, text);
    }

    @GetMapping("/employees/find/{text}")
    public List<Employee> getSearchedEmployees(@PathVariable("text") String text) {
        return employeeService.findEmployeesOfFullList(text);
    }

    @GetMapping("/employees/check/{agentId}")
    public boolean checkEmployee(@PathVariable("agentId") String agentId) {
        return employeeService.checkEmployee(agentId);
    }
}
