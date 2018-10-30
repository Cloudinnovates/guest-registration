package com.exadel.guestregistration.services.cards.mock.v1_with_spring_mockbean_mockito_mock;


import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import com.exadel.guestregistration.repositories.CardRepository;
import com.exadel.guestregistration.repositories.OfficeRepository;
import com.exadel.guestregistration.services.CardService;
import com.exadel.guestregistration.services.impl.CardServiceImpl;


public class CardServiceMockTestConfiguration {


    //1 VIENAS Varaintas
    @MockBean
    private CardRepository cardRepository;


    //2 KITAS VARIANTAS
    @Bean
    public OfficeRepository officeRepository() {
        return Mockito.mock(OfficeRepository.class);
    }

    @Bean
    public CardService cardService() {
        return new CardServiceImpl();
    } 
}
