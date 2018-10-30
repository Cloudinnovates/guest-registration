package com.exadel.guestregistration.services;

import com.exadel.guestregistration.exceptions.NotFoundException;
import com.exadel.guestregistration.models.Office;
import com.exadel.guestregistration.repositories.OfficeRepository;
import com.exadel.guestregistration.services.impl.OfficeServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class OfficeServiceMockitoTest {

    @Mock
    private OfficeRepository officeRepositoryMock;

    @InjectMocks
    private OfficeServiceImpl officeService;

    @Captor
    private ArgumentCaptor<Office> officeArgumentCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        officeArgumentCaptor = ArgumentCaptor.forClass(Office.class);
    }

    @Test
    public void findAll() {
        Office firstOffice = newOffice();
        Office secondOffice = newOffice();
        when(officeRepositoryMock.findAll()).thenReturn(Arrays.asList(firstOffice, secondOffice));
        List<Office> offices = officeService.findAll();
        assertFalse(offices.isEmpty());
        assertEquals(2, offices.size());
        verify(officeRepositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(officeRepositoryMock);
    }

    @Test
    public void create() {
        Office office = newOffice();
        officeRepositoryMock.save(office);
        when(officeRepositoryMock.save(any(Office.class))).thenReturn(new Office());
        verify(officeRepositoryMock, times(1)).save(any(Office.class));
        assertThat(officeRepositoryMock.save(office), is(notNullValue()));
        verify(officeRepositoryMock, never()).delete(any(Office.class));

        verify(officeRepositoryMock, times(2)).save(officeArgumentCaptor.capture());
        verifyNoMoreInteractions(officeRepositoryMock);

        assertNotNull(officeArgumentCaptor.getValue());
        assertNotNull(officeArgumentCaptor.getValue().getId());
        assertEquals(office.getName(), officeArgumentCaptor.getValue().getName());
        assertEquals(office.getPhone(), officeArgumentCaptor.getValue().getPhone());
        assertEquals(office.getEmail(), officeArgumentCaptor.getValue().getEmail());
        assertEquals(office.getType(), officeArgumentCaptor.getValue().getType());
        assertEquals(office.getManager_name(), officeArgumentCaptor.getValue().getManager_name());
        assertEquals(office.getManager_surname(), officeArgumentCaptor.getValue().getManager_surname());
        assertEquals(office.getManager_email(), officeArgumentCaptor.getValue().getManager_email());
    }

    @Test
    public void update() throws NotFoundException {
        Office office = newOffice();
        officeRepositoryMock.save(office);
        when(officeRepositoryMock.save(any(Office.class))).thenReturn(new Office());
        when(officeRepositoryMock.findById(office.getId())).thenReturn(java.util.Optional.of(office));
        assertTrue(officeRepositoryMock.findById(office.getId()).isPresent());

        Office foundOffice = officeRepositoryMock.findById(office.getId()).get();
        assertNotNull(foundOffice);
        assertEquals(office, foundOffice);
        foundOffice.setName("Amsterdam");
        foundOffice.setManager_name("Manager");
        foundOffice.setManager_surname("Reminson");
        officeRepositoryMock.save(foundOffice);
        when(officeService.update(foundOffice)).thenReturn(new Office());

        when(officeRepositoryMock.findById(foundOffice.getId())).thenReturn(java.util.Optional.of(foundOffice));
        assertEquals(foundOffice.getName(), "Amsterdam");
        assertEquals(foundOffice.getManager_name(), "Manager");
        assertEquals(foundOffice.getManager_surname(), "Reminson");

        verify(officeRepositoryMock, times(2)).save(any(Office.class));
    }

    @Test
    public void delete() throws NotFoundException {
        Office office = newOffice();
        when(officeRepositoryMock.findById(office.getId())).thenReturn(java.util.Optional.of(office));
        assertNotNull(officeRepositoryMock.findById(office.getId()));

        doNothing().when(officeRepositoryMock).deleteById(office.getId());
        officeService.delete(office.getId());

        verify(officeRepositoryMock, times(1)).deleteById(office.getId());
    }

    @Test
    public void findOfficeById() {
        if (officeRepositoryMock.findAll().size() == 0) {
            when(officeRepositoryMock.findById(anyString())).thenReturn(null);
        }

        Office office = newOffice();
        when(officeRepositoryMock.findById(office.getId())).thenReturn(java.util.Optional.of(office));
        assertTrue(officeRepositoryMock.findById(office.getId()).isPresent());

        Office foundOffice = officeRepositoryMock.findById(office.getId()).get();
        assertEquals(foundOffice, office);
        verify(officeRepositoryMock, times(2)).findById(office.getId());
    }

    @Test
    public void findOffices() {
        if (officeRepositoryMock.findAll().size() == 0) {
            when(officeRepositoryMock.findOfficesByText(anyString())).thenReturn(null);
        }

        Office firstOffice = newOffice();
        Office secondOffice = newOffice();
        List <Office> offices = Arrays.asList(firstOffice, secondOffice);
        assertNotNull(offices);
        assertFalse(offices.isEmpty());

        when(officeRepositoryMock.findOfficesByText(firstOffice.getName())).thenReturn(offices);
        secondOffice.setName("Monaco");
        assertEquals(officeRepositoryMock.findOfficesByText(firstOffice.getName()).get(0), firstOffice);
        assertNotEquals(officeRepositoryMock.findOfficesByText(firstOffice.getName()).get(0).getName(), secondOffice.getName());
        verify(officeRepositoryMock, times(2)).findOfficesByText(firstOffice.getName());
    }

    private Office newOffice() {
        return new Office(""+(Math.random()*10000)+"", "Paris", "Address 1",
                "Phone No. 1", "Some email", "Some type",
                "Name", "Surname", "Email");
    }
}
