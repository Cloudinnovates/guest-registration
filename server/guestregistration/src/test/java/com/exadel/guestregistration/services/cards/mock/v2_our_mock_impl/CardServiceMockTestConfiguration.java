package com.exadel.guestregistration.services.cards.mock.v2_our_mock_impl;


import java.util.List;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.exadel.guestregistration.models.Card;
import com.exadel.guestregistration.models.Office;
import com.exadel.guestregistration.repositories.CardRepository;
import com.exadel.guestregistration.repositories.OfficeRepository;
import com.exadel.guestregistration.services.CardService;
import com.exadel.guestregistration.services.impl.CardServiceImpl;


public class CardServiceMockTestConfiguration {


    @Bean
    public OfficeRepository officeRepository() {
        return new OfficeRepositoryMock();
    }

    @Bean
    public CardRepository cardRepository() {
        return new CardRepositoryMock();     
    }

    @Bean
    public CardService cardService() {
        return new CardServiceImpl();
    }
}

class CardRepositoryMock implements CardRepository {

    public int saveCount = 0;

    @Override
    public <S extends Card> S save(S s) {
        saveCount++;
        return null;
    }

    @Override
    public <S extends Card> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<Card> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<Card> findAll() {
        return null;
    }

    @Override
    public Iterable<Card> findAllById(Iterable<String> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(Card card) {

    }

    @Override
    public void deleteAll(Iterable<? extends Card> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Card> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Card> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Card> S insert(S s) {
        return null;
    }

    @Override
    public <S extends Card> List<S> insert(Iterable<S> iterable) {
        return null;
    }

    @Override
    public <S extends Card> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Card> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Card> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Card> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Card> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Card> boolean exists(Example<S> example) {
        return false;
    }
}

class OfficeRepositoryMock implements OfficeRepository {

    @Override
    public List<Office> findOfficesByText(String text) {
        return null;
    }

    @Override
    public <S extends Office> S save(S s) {
        return null;
    }

    @Override
    public <S extends Office> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<Office> findById(String s) {
        return s.equals(CardServiceMockTest.OFFICE_ID) ? Optional.of(new Office()) : Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<Office> findAll() {
        return null;
    }

    @Override
    public Iterable<Office> findAllById(Iterable<String> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(Office office) {

    }

    @Override
    public void deleteAll(Iterable<? extends Office> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Office> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Office> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Office> S insert(S s) {
        return null;
    }

    @Override
    public <S extends Office> List<S> insert(Iterable<S> iterable) {
        return null;
    }

    @Override
    public <S extends Office> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Office> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Office> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Office> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Office> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Office> boolean exists(Example<S> example) {
        return false;
    }
}
