package com.exadel.guestregistration.services.impl;

import com.exadel.guestregistration.exceptions.NotFoundException;
import com.exadel.guestregistration.models.Office;
import com.exadel.guestregistration.repositories.OfficeRepository;
import com.exadel.guestregistration.services.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class OfficeServiceImpl implements OfficeService {

    @Autowired
    private OfficeRepository officeRepository;


    @Override
    public List<Office> findAll() {
        return officeRepository.findAll();
    }

    @Override
    public Office create(Office office) {
        return officeRepository.insert(office);
    }

    @Override
    public Office findOfficeById(String id) throws NotFoundException { 
        if (officeRepository.findById(id).isPresent()) {
            return officeRepository.findById(id).get();
        } else {
            throw new NotFoundException("Office with the provided id not found!");
        }
    }

    @Override
    public void delete(String id) throws NotFoundException {
        if (officeRepository.findById(id).isPresent()) {
            officeRepository.deleteById(id);
        } else {
            throw new NotFoundException("Office with the provided id not found!");
        }
    }

    @Override
    public Office update(Office office)throws NotFoundException {
        if (officeRepository.findById(office.getId()).isPresent()) {
            Optional<Office> foundOffice = officeRepository.findById(office.getId());
            Office officeData = foundOffice.get();
            officeData.setName(office.getName());
            officeData.setAddress(office.getAddress());
            officeData.setPhone(office.getPhone());
            officeData.setEmail(office.getEmail());
            officeData.setType(office.getType());
            officeData.setManager_name(office.getManager_name());
            officeData.setManager_surname(office.getManager_surname());
            officeData.setManager_email(office.getManager_email());
            return officeRepository.save(officeData);
        } else {
            throw new NotFoundException("Office with the provided id not found!");
        }
    }

    @Override
    public List<Office> findOffices(String text) {
        return officeRepository.findOfficesByText(text);
    }

}
