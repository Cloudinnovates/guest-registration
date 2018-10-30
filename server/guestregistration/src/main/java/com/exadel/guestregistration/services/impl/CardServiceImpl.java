package com.exadel.guestregistration.services.impl;

import com.exadel.guestregistration.exceptions.NotFoundException;
import com.exadel.guestregistration.exceptions.ParameterException;
import com.exadel.guestregistration.models.Card;
import com.exadel.guestregistration.models.CardTypes;
import com.exadel.guestregistration.models.Office;
import com.exadel.guestregistration.repositories.CardRepository;
import com.exadel.guestregistration.repositories.OfficeRepository;
import com.exadel.guestregistration.services.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CardServiceImpl implements CardService {
    private static final Logger logger = LoggerFactory.getLogger(CardServiceImpl.class);

    @Autowired
    private CardRepository cardRepository;


    //TODO: please depend not directly form DAO, use Office Service!
    @Autowired
    private OfficeRepository officeRepository;

    @Override
    public List<Card> getCards() { 
        return cardRepository.findAll();
    }

    @Override
    public Card getCard(String id) throws NotFoundException {
        Optional<Card> card = cardRepository.findById(id);
        if (card.isPresent()){
            return card.get();
        }
        logger.info("card with id("+id+") not found");
        throw new NotFoundException("card not found");
    }

    @Override
    public Card createCard(Card card) throws ParameterException {
        if (CardTypes.isTypeCorrect(card.getCardType()) && officeRepository.findById(card.getOfficeId()).isPresent() && card.getValidFrom().before(card.getValidThru())) {
            return cardRepository.save(card);
        } else {
            logger.debug("card param isn't correct, card - "+card.toString());
            throw new ParameterException("card parameter isn't correct");
        }
    }

    @Override
    public void deleteCard(String id) throws NotFoundException {
        Optional<Card> card = cardRepository.findById(id);
        if (card.isPresent()){
            cardRepository.deleteById(id);
        }else{
            logger.info("card with id("+id+") not found couldn't delete");
            throw new NotFoundException("card not found");
        }

    }

    @Override
    public Card updateCard(Card card) throws ParameterException, NotFoundException {
        Optional<Card> foundCard = cardRepository.findById(card.getId());
        if(foundCard.isPresent()){
            Card cardData = foundCard.get();
            cardData.setCardNo(card.getCardNo());
            cardData.setCardType(card.getCardType());
            cardData.setCardAvailable(card.isCardAvailable());
            cardData.setOfficeId(card.getOfficeId());
            cardData.setValidFrom(card.getValidFrom());
            cardData.setValidThru(card.getValidThru());

            if (CardTypes.isTypeCorrect(card.getCardType()) && officeRepository.findById(card.getOfficeId()).isPresent() && card.getValidFrom().before(card.getValidThru())) {
                return cardRepository.save(cardData);
            } else {
                logger.debug("card param isn't correct, card - "+card.toString());
                throw new ParameterException("card parameter isn't correct");
            }
        }else {
            logger.info("card with id("+card.getId()+") not found couldn't update");
            throw new NotFoundException("card not found");
        }

    }

    @Override
    public List<Card> findCards(String text, String type) {
        List<Card> allCards = cardRepository.findAll();
        List<Card> foundCards = new ArrayList<Card>();
        text = text.toLowerCase();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        switch (type) {
            case "cardNo":
                for (Card card : allCards) {
                    if ((card.getCardNo() + "").toLowerCase().contains(text)) {
                        foundCards.add(card);
                    }
                }
                break;
            case "validFrom":
                for (Card card : allCards) {
                    if (sdf.format(card.getValidFrom()).contains(text)) {
                        foundCards.add(card);
                    }
                }
                break;
            case "validThru":
                for (Card card : allCards) {
                    if (sdf.format(card.getValidThru()).contains(text)) {
                        foundCards.add(card);
                    }
                }
                break;
            case "office":
                for (Card card : allCards) {
                    Optional<Office> officeOpt = officeRepository.findById(card.getOfficeId());
                    if(officeOpt.isPresent()){
                        Office office = officeOpt.get();
                        if (office.getName().toLowerCase().contains(text)) {
                            foundCards.add(card);
                        }
                    }
                }
                break;
            case "cardType":
                for (Card card : allCards) {
                    if (card.getCardType().toLowerCase().contains(text)) {
                        foundCards.add(card);
                    }
                }
                break;
            case "availability":
                for (Card card : allCards) {
                    if (text.equals("true") && card.isCardAvailable()) {
                        foundCards.add(card);
                    }else if(text.equals("false") && !card.isCardAvailable()){
                        foundCards.add(card);
                    }
                }
                break;
            default:
                break;
        }
        return foundCards;
    }

    @Override
    public List<String> getAllTypes() {
        return CardTypes.getAllTypes();
    }

    @Override
    public List<Card> getAvailableCards() {
        List<Card> availableCards = new ArrayList<>();
        List<Card> allCards = cardRepository.findAll();
        for (int i = 0; i < allCards.size(); i++) {
            if (allCards.get(i).isCardAvailable()){
                availableCards.add(allCards.get(i));
            }
        }
        return availableCards;
    }
}
