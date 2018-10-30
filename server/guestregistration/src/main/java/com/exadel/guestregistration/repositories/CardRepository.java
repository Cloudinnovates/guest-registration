package com.exadel.guestregistration.repositories;

import com.exadel.guestregistration.models.Card;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CardRepository extends MongoRepository<Card, String> {
}
