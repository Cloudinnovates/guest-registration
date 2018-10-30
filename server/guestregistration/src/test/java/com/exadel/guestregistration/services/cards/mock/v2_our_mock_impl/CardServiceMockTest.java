package com.exadel.guestregistration.services.cards.mock.v2_our_mock_impl;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.exadel.guestregistration.models.Card;
import com.exadel.guestregistration.models.CardTypes;
import com.exadel.guestregistration.repositories.CardRepository;
import com.exadel.guestregistration.repositories.OfficeRepository;
import com.exadel.guestregistration.services.CardService;
import com.exadel.guestregistration.services.impl.CardServiceImpl;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CardServiceMockTestConfiguration.class)
public class CardServiceMockTest {


    public static final String OFFICE_ID = "ID-1";

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private CardService cardService;


    @Before
    public void setup() {

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
        assertTrue(cardRepository instanceof CardRepositoryMock);
        assertTrue(officeRepository instanceof OfficeRepositoryMock);

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

        assertEquals(1, ((CardRepositoryMock) cardRepository).saveCount);
    }

}
