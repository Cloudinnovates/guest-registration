package com.exadel.guestregistration.services;

import com.exadel.guestregistration.exceptions.NotFoundException;
import com.exadel.guestregistration.models.Office;
import com.exadel.guestregistration.repositories.OfficeRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class OfficeServiceTest {

    @Autowired
    private OfficeRepository officeRepository;
    @Autowired
    private OfficeService officeService;

    @Before
    public void setup() {
        Assert.assertNotNull(officeService);
        Assert.assertNotNull(officeRepository);
    }

    @Test
    public void findAll() throws NotFoundException {
        Office office = newOffice();
        officeRepository.save(office);
        List<Office> offices = officeRepository.findAll();

        Assert.assertNotNull(offices);
        Assert.assertEquals(offices.size(), officeRepository.findAll().size());

        Office anotherOffice = newOffice();
        officeRepository.save(anotherOffice);

        Assert.assertEquals(offices.size() + 1, officeRepository.findAll().size());
        officeRepository.deleteById(office.getId());
        Assert.assertEquals(offices.size(), officeRepository.findAll().size());

        officeRepository.save(office);
        Office foundOffice = officeService.findOfficeById(offices.get(offices.size() - 1).getId());
        Assert.assertEquals(officeRepository.findAll().get(officeRepository.findAll().size() - 1).getId(), foundOffice.getId());

        officeRepository.deleteById(office.getId());
        officeRepository.deleteById(anotherOffice.getId());
    }

    @Test
    public void create() throws NotFoundException {
        Office office = newOffice();
        officeRepository.save(office);
        Office foundOffice = officeService.findOfficeById(office.getId());

        Assert.assertNotNull(foundOffice);
        Assert.assertEquals(office.getName(), foundOffice.getName());
        Assert.assertEquals(office.getAddress(), foundOffice.getAddress());
        Assert.assertEquals(office.getEmail(), foundOffice.getEmail());
        Assert.assertEquals(office.getPhone(), foundOffice.getPhone());
        Assert.assertEquals(office.getType(), foundOffice.getType());
        Assert.assertEquals(office.getManager_name(), foundOffice.getManager_name());
        Assert.assertEquals(office.getManager_surname(), foundOffice.getManager_surname());
        Assert.assertEquals(office.getManager_email(), foundOffice.getManager_email());

        officeRepository.deleteById(office.getId());
    }

    @Test
    public void update() throws NotFoundException {
        Office firstOffice = newOffice();
        Office secondOffice = newOffice();
        Assert.assertNotEquals(firstOffice.getId(), secondOffice.getId());
        Assert.assertEquals(firstOffice.getEmail(), secondOffice.getEmail());

        secondOffice.setId(firstOffice.getId());
        Assert.assertEquals(firstOffice.getId(), secondOffice.getId());

        secondOffice.setId("1234567890");
        secondOffice.setEmail("another.email@someaddress.com");
        officeRepository.save(secondOffice);

        Assert.assertTrue(officeRepository.findById(secondOffice.getId()).isPresent());
        Assert.assertNotEquals(firstOffice.getId(), officeService.findOfficeById(secondOffice.getId()).getId());
        Assert.assertEquals(firstOffice.getName(), secondOffice.getName());
        Assert.assertNotEquals(firstOffice.getEmail(), officeService.findOfficeById(secondOffice.getId()).getEmail());

        officeRepository.deleteById(secondOffice.getId());
    }

    @Test
    public void delete() throws NotFoundException {
        Office office = newOffice();
        officeRepository.save(office);

        Assert.assertTrue(officeRepository.findById(office.getId()).isPresent());
        officeService.delete(office.getId());
        Assert.assertFalse(officeRepository.findById(office.getId()).isPresent());
    }

    @Test
    public void findOfficeById() throws NotFoundException {
        Office office = newOffice();
        officeRepository.save(office);
        Office foundOffice = officeService.findOfficeById(office.getId());

        Assert.assertEquals(foundOffice.getName(), office.getName());
        Assert.assertEquals(foundOffice.getAddress(), office.getAddress());
        Assert.assertEquals(foundOffice.getPhone(), office.getPhone());
        Assert.assertEquals(foundOffice.getEmail(), office.getEmail());
        Assert.assertEquals(foundOffice.getType(), office.getType());
        Assert.assertEquals(foundOffice.getManager_name(), office.getManager_name());
        Assert.assertEquals(foundOffice.getManager_surname(), office.getManager_surname());
        Assert.assertEquals(foundOffice.getManager_email(), office.getManager_email());

        officeRepository.deleteById(office.getId());
    }

    @Test
    public void findOffices() throws NotFoundException {
        Office firstOffice = newOffice();
        Office secondOffice = newOffice();
        officeRepository.save(firstOffice);
        officeRepository.save(secondOffice);

        List<Office> offices = officeRepository.findOfficesByText(firstOffice.getName());
        Assert.assertNotNull(offices.get(0));
        Assert.assertFalse(offices.isEmpty());
        Assert.assertTrue(offices.size() >= 2);

        offices = officeRepository.findOfficesByText(firstOffice.getEmail().substring(0, 3));
        Assert.assertFalse(offices.isEmpty());
        Assert.assertTrue(offices.size() > 1);
        Assert.assertFalse(offices.get(0).getEmail().isEmpty());

        if (offices.size() > 1) {
            Assert.assertNotSame(offices.get(0), offices.get(1));
        }

        officeRepository.deleteById(firstOffice.getId());
        officeRepository.deleteById(secondOffice.getId());
    }

    private Office newOffice() {
        return new Office(""+(Math.random()*10000)+"", "Paris", "Address 1",
                        "Phone No. 1", "Some email", "Some type",
                        "Name", "Surname", "Email");
    }
}