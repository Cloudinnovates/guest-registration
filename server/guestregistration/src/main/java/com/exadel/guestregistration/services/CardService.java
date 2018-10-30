package com.exadel.guestregistration.services;

import com.exadel.guestregistration.exceptions.NotFoundException;
import com.exadel.guestregistration.exceptions.ParameterException;
import com.exadel.guestregistration.models.Card;

import java.util.List;

public interface CardService {

    /**
     * returns all cards in the database
     * @return
     */
    public List<Card> getCards();

    /**
     *
     * @param id id of the card
     * @return card with the provided id
     */
    public Card getCard(String id) throws NotFoundException;

    /**
     * creates card with supplied data
     * @param card card data
     * @return response entity
     */
    public Card createCard(Card card) throws ParameterException;

    /**
     * deletes card with provided id
     * @param id
     * @return response entity
     */
    public void deleteCard(String id) throws NotFoundException;

    /**
     * updates card with the same id
     * @param id card id that user wants to update
     * @param card updated card data
     * @return response entity with updated card data
     */
    public Card updateCard(Card card) throws ParameterException, NotFoundException;

    /**
     * returns list of cards that contains provided phrase
     * @param text search phrase
     * @return list of cards
     */
    public List<Card> findCards(String text, String type);

    /**
     * returns all available types of the card
     * @return
     */
    public List<String> getAllTypes();

    List<Card> getAvailableCards();
}
