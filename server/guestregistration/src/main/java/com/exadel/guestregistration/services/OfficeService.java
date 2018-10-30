package com.exadel.guestregistration.services;

import com.exadel.guestregistration.exceptions.NotFoundException;
import com.exadel.guestregistration.models.Office;
import java.util.List;


public interface OfficeService {
    List<Office> findAll();
    Office create(Office office);
    Office update(Office office) throws NotFoundException;
    void delete(String id) throws NotFoundException;
    Office findOfficeById(String id) throws NotFoundException;
    List<Office> findOffices(String text);
}
