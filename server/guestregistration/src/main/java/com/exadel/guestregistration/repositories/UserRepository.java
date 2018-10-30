package com.exadel.guestregistration.repositories;

import com.exadel.guestregistration.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Repository to retrieve user data from database
 */
public interface UserRepository extends MongoRepository<User,String > {

    @Query("{'username': ?0}")
    Optional<User> findUserByName(String username);
}
