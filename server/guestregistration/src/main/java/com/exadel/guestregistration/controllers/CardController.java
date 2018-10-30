package com.exadel.guestregistration.controllers;

import com.exadel.guestregistration.exceptions.NotFoundException;  
import com.exadel.guestregistration.exceptions.ParameterException;
import com.exadel.guestregistration.models.Card;
import com.exadel.guestregistration.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api/cards")
@CrossOrigin(origins = "*")
public class CardController {
    @Autowired
    CardService cardService;

    /**
     * sends response with all cards in the database
     * @return
     */
    @GetMapping()
    public List<Card> getAllCards(){
        return cardService.getCards();
    }

    /**
     * creates new card with provided data, if creation is successful sends card data
     * that was created back to the user if not user receives error message
     * @param card data
     * @return response entity
     */
    @PostMapping()
    public ResponseEntity<?> createCard(@Valid @RequestBody Card card){
        try {
            Card cardResponse = cardService.createCard(card);
            return ResponseEntity.ok().body(cardResponse);
        } catch (ParameterException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("One of the parameters incorrect");
        }
    }

    /**
     * deletes card with matching id that was provided
     * @param id card id
     * @return response entity
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteCard(@PathVariable("id") String id){
        try {
            cardService.deleteCard(id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: card not found");
        }

    }

    /**
     * updates card with matching id to the data provided. If update is successful
     * response with updated card data is sent, if not error response is sent
     * @param card card data
     * @return response entity
     */
    @PutMapping()
    public ResponseEntity<?> putCard(@Valid @RequestBody Card card){
        try {
            return ResponseEntity.ok().body(cardService.updateCard(card));
        } catch (ParameterException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("One of the parameters incorrect");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: card not found");
        }
    }

    /**
     * gets single card with matching id if it is found
     * @param id card id
     * @return card details
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getSingleCard(@PathVariable("id") String id){
        try {
            return ResponseEntity.ok().body(cardService.getCard(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: card not found");
        }
    }

    /**
     * returns list of all cards that contains search phrase.
     * @param text search phrase
     * @return list of found cards
     */
    @RequestMapping(
            value = "find",
            params = { "query", "type" },
            method = GET)
    public List<Card> findCards (@RequestParam("query") String text, @RequestParam("type") String type){
        return cardService.findCards(text,type);
    }

    @GetMapping("/types")
    public List<String> getTypes(){
        return cardService.getAllTypes();
    }

    @GetMapping("/available")
    public List<Card> getAvailableCards(){
        return cardService.getAvailableCards();
    }



}
