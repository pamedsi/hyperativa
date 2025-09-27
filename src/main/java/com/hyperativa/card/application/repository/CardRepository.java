package com.hyperativa.card.application.repository;


import org.springframework.stereotype.Repository;

import java.util.List;

import com.hyperativa.card.domain.Card;
import com.hyperativa.user.domain.User;

@Repository
public interface CardRepository {

    void save(Card newCard);

    boolean existsCardByNumber(String cardNumberHash);

    void saveAll(List<Card> cards);

    Card getCardByOwner(User owner, String hashCardNumber);

}
