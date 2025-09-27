package com.hyperativa.card.application.repository;


import com.hyperativa.card.domain.Card;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository {

    void save(Card newCard);

    boolean existsCardByNumber(String cardNumberHash);

}
