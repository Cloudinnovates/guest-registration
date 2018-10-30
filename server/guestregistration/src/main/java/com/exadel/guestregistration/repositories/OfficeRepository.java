package com.exadel.guestregistration.repositories;

import com.exadel.guestregistration.models.Office;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfficeRepository extends MongoRepository<Office, String> {

    @Query("{$or: [{'name': {$regex: ?0, $options: 'i'}}, {'address': {$regex: ?0, $options: 'i'}}," +
            " {'phone': {$regex: ?0, $options: 'i'}}, {'email': {$regex: ?0, $options: 'i'}}," +
            " {'type': {$regex: ?0, $options: 'i'}}, {'manager_name': {$regex: ?0, $options: 'i'}}," +
            " {'manager_surname': {$regex: ?0, $options: 'i'}}, {'manager_email': {$regex: ?0, $options: 'i'}}]}")
    List<Office> findOfficesByText(String text);
}