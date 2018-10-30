package com.exadel.guestregistration.services.cards.mock.v1_with_spring_mockbean_mockito_mock;


import static org.junit.Assert.assertNotNull;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.exadel.guestregistration.models.Card;
import com.exadel.guestregistration.models.CardTypes;
import com.exadel.guestregistration.models.Office;
import com.exadel.guestregistration.repositories.CardRepository;
import com.exadel.guestregistration.repositories.OfficeRepository;
import com.exadel.guestregistration.services.CardService;
import com.exadel.guestregistration.services.impl.CardServiceImpl;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CardServiceMockTestConfiguration.class)
public class CardServiceMockTest {


    private static final String OFFICE_ID = "ID-1"; 
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private CardService cardService;


    @Before
    public void setup() {
        Mockito.when(officeRepository.findById(OFFICE_ID)).thenReturn(Optional.of(new Office()));
    }

    @After
    public void clean() {

    }

    @Test
    public void testLoadedServiceAndDAOs() {
        assertNotNull(cardRepository);
        assertNotNull(officeRepository);
        assertNotNull(cardService);

        //patikrinam ar servisas injectintas tinkamas
        assertTrue(cardService instanceof CardServiceImpl);

        //pasitikrinam ar repozitorijos netikros
        assertTrue(cardRepository.getClass().getSimpleName().contains("$MockitoMock$"));
        assertTrue(officeRepository.getClass().getSimpleName().contains("$MockitoMock$"));

    }


    //testuojam metodus
    @Test
    public void test_createCard() throws Exception {


        Card card = new Card();
        card.setCardType(CardTypes.GUEST);
        card.setCardNo(1);
        card.setValidFrom(new Date(LocalDate.of(2018, 2, 2).toEpochDay()));
        card.setValidThru(new Date(LocalDate.of(2018, 10, 2).toEpochDay()));
        card.setOfficeId(OFFICE_ID);

        cardService.createCard(card);

        Mockito.verify(cardRepository, times(1)).save(any());
    }

}
