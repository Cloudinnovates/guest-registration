package com.exadel.guestregistration.controllers;

import com.exadel.guestregistration.exceptions.NotFoundException;
import com.exadel.guestregistration.models.Office;
import com.exadel.guestregistration.services.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class OfficeController {

    @Autowired
    private OfficeService officeService;

    @GetMapping("/office_register/getAll")
    public List<Office> getAllOffices() {
        return officeService.findAll();
    }

    @PostMapping("/office_register/create")
    public Office createOffice(@Valid @RequestBody Office office) {
        return officeService.create(office);
    }

    @GetMapping(value = "/office_register/{id}")
    public ResponseEntity<?> getOfficeById(@PathVariable("id") String id) {
        try {
            return ResponseEntity.ok().body(officeService.findOfficeById(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Office was not found");
        }
    }

    @PutMapping(value="/office_register/update/{id}")
    public ResponseEntity<?> updateOffice(@PathVariable("id") String id, @Valid @RequestBody Office office) {
        try {
            return ResponseEntity.ok().body(officeService.update(office));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Office was not found");
        }
    }

    @DeleteMapping(value="/office_register/delete/{id}")
    public ResponseEntity<?> deleteOffice(@PathVariable("id") String id) {
        try {
            officeService.delete(id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Office was not found");
        }
    }

    @GetMapping("office_register/find/{text}")
    public List<Office> getOffices(@PathVariable("text") String text) {
        return officeService.findOffices(text);
    }
}
