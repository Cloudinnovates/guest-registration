package com.exadel.guestregistration.services.cards;


import com.exadel.guestregistration.exceptions.NotFoundException;
import com.exadel.guestregistration.exceptions.ParameterException;
import com.exadel.guestregistration.models.Card;
import com.exadel.guestregistration.models.CardTypes;
import com.exadel.guestregistration.models.Office;
import com.exadel.guestregistration.repositories.CardRepository;
import com.exadel.guestregistration.repositories.OfficeRepository;
import com.exadel.guestregistration.services.CardService;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CardServiceTest {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private CardService cardService;


    Office office = new Office(""+(Math.random()*10000)+"", "Paris", "Address 1",
            "Phone No. 1", "Some email", "Some type",
            "Name", "Surname", "Email");


    @Before
    public void setup() {
        Assert.assertNotNull(cardService);
        Assert.assertNotNull(cardRepository);
        officeRepository.save(office);
    }

    @After
    public void clean(){
        cardRepository.deleteAll();
        officeRepository.deleteAll();
    }

    private Card createNewCard(){
        return new Card(""+(Math.random()*1000000)+"", (int)(Math.random()*1000), new Date(1537522766078L), new Date(1593162000000L), office.getId(), CardTypes.getAllTypes().get((int)(Math.random()*CardTypes.getAllTypes().size())), true);
    }

    @Test
    public void testGetAllTypes() throws Exception{
        List<String> serviceTypes = cardService.getAllTypes();
        List<String> types = CardTypes.getAllTypes();
        for (int i = 0; i < types.size(); i++) {
            Assert.assertEquals(serviceTypes.get(i), types.get(i));
        }
    }

    @Test
    public void testGetAllCards() throws Exception{
        int cardCount = 3;
        List<Card> cards = new ArrayList<>();
        Card newCard = createNewCard();
        for (int i = 0; i < cardCount; i++) {
            newCard.setId(""+(Math.random()*1000000-i)+"");
            newCard.setCardNo(i);
            cardRepository.save(newCard);
            cards = cardService.getCards();
            Card card = cards.get(cards.size()-1);
            Assert.assertEquals(card.getId(), newCard.getId());
            Assert.assertEquals(card.getCardNo(), newCard.getCardNo());
            Assert.assertEquals(card.getValidFrom(), newCard.getValidFrom());
            Assert.assertEquals(card.getValidThru(), newCard.getValidThru());
            Assert.assertEquals(card.getCardType(), newCard.getCardType());
            Assert.assertEquals(card.getOfficeId(), newCard.getOfficeId());
            Assert.assertEquals(card.isCardAvailable(), newCard.isCardAvailable());
        }
        Assert.assertEquals(cards.size(), cardCount);
    }

    @Test
    public void testCreateCard() throws Exception{
        Card newCard = createNewCard();
        cardService.createCard(newCard);
        Card foundCard =  cardRepository.findById(newCard.getId()).get();
        Assert.assertNotNull(foundCard);
        Assert.assertEquals(foundCard.getCardNo(), newCard.getCardNo());
        Assert.assertEquals(foundCard.getValidFrom(), newCard.getValidFrom());
        Assert.assertEquals(foundCard.getValidThru(), newCard.getValidThru());
        Assert.assertEquals(foundCard.getCardType(), newCard.getCardType());
        Assert.assertEquals(foundCard.getOfficeId(), newCard.getOfficeId());
        Assert.assertEquals(foundCard.isCardAvailable(), newCard.isCardAvailable());

    }

    @Test
    public void testGetCard() throws Exception{
        Card newCard = createNewCard();
        cardRepository.save(newCard);
        Card foundCard = cardService.getCard(newCard.getId());
        Assert.assertEquals(foundCard.getCardNo(), newCard.getCardNo());
        Assert.assertEquals(foundCard.getValidFrom(), newCard.getValidFrom());
        Assert.assertEquals(foundCard.getValidThru(), newCard.getValidThru());
        Assert.assertEquals(foundCard.getCardType(), newCard.getCardType());
        Assert.assertEquals(foundCard.getOfficeId(), newCard.getOfficeId());
        Assert.assertEquals(foundCard.isCardAvailable(), newCard.isCardAvailable());
    }

    @Test
    public void testDeleteCard() throws Exception{
        Card newCard = createNewCard();
        cardRepository.save(newCard);
        Assert.assertTrue(cardRepository.findById(newCard.getId()).isPresent());
        cardService.deleteCard(newCard.getId());
        Assert.assertFalse(cardRepository.findById(newCard.getId()).isPresent());
    }

    @Test
    public void testUpdateCard() throws Exception{
        Card card1 = createNewCard();
        Card card2 = createNewCard();
        while (card1.getCardType().equals(card2.getCardType())){
            card2.setCardType(CardTypes.getAllTypes()
                    .get((int)(Math.random()*CardTypes.getAllTypes().size())));
        }
        card2.setId(card1.getId());
        Assert.assertNotEquals(card1.getCardType(), card2.getCardType());
        cardRepository.save(card1);
        Assert.assertTrue(cardRepository.findById(card1.getId()).isPresent());
        cardService.updateCard(card2);
        Card foundCard = cardService.getCard(card1.getId());
        Assert.assertEquals(foundCard.getCardType(), card2.getCardType());
    }

    @Test(expected = NotFoundException.class)
    public void testGetCardWrongId() throws Exception{
        Card newCard = createNewCard();
        cardRepository.save(newCard);
        newCard.setId("not_valid");
        cardService.getCard(newCard.getId());

    }

    @Test(expected = NotFoundException.class)
    public void testDeleteCardWrongId() throws Exception{
        Card newCard = createNewCard();
        cardRepository.save(newCard);
        newCard.setId("not_valid");
        cardService.deleteCard(newCard.getId());
    }

    @Test(expected = ParameterException.class)
    public void testCreateCardWrongTime() throws  Exception{
        Card newCard = createNewCard();
        Date placehopder = newCard.getValidFrom();
        newCard.setValidFrom(newCard.getValidThru());
        newCard.setValidThru(placehopder);
        cardService.createCard(newCard);
    }

    @Test(expected = ParameterException.class)
    public void testCreateCardWrongType() throws Exception{
        Card newCard = createNewCard();
        newCard.setCardType("Fake_Type");
        cardService.createCard(newCard);
    }

    @Test(expected = DuplicateKeyException.class)
    public void testCreateCardDuplicateCardNo() throws Exception{
        Card card1 = createNewCard();
        Card card2 = createNewCard();
        card2.setCardNo(card1.getCardNo());
        cardService.createCard(card1);
        cardService.createCard(card2);
    }

    @Test
    public void testSearch() throws Exception{
        int cardAmount = 10;
        List<Card> result;
        int amountOfType = 0;
        String searchType = CardTypes.getAllTypes().get((int)(Math.random()*CardTypes.getAllTypes().size()));
        ArrayList<Card> cards = fillCards(cardAmount);
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getCardType().equals(searchType)){
                amountOfType++;
            }
            result = cardService.findCards( String.valueOf(cards.get(i).getCardNo()),"cardNo");
            Assert.assertTrue(result.get(0).getId().equals(cards.get(i).getId()));
            Assert.assertTrue(result.size() == 1);
        }
        result = cardService.findCards( "Paris","office");
        Assert.assertTrue(result.size() == cardAmount);
        result = cardService.findCards( "null","office");
        Assert.assertTrue(result.size() == 0);
        result = cardService.findCards( "true","availability");
        Assert.assertTrue(result.size() == cardAmount);
        result = cardService.findCards( "false","availability");
        Assert.assertTrue(result.size() == 0);
        result = cardService.findCards(searchType, "cardType");
        Assert.assertTrue(result.size() == amountOfType);

    }

    public ArrayList<Card> fillCards(int amount){
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            Card card = createNewCard();
            card.setCardNo(i);
            cards.add(card);
            cardRepository.save(card);
        }
        return cards;
    }
}
