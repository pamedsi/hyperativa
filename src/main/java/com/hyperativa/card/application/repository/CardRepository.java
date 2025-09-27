package com.hyperativa.card.application.repository;


import com.hyperativa.card.domain.Card;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository {

    void save(Card newCard);

    boolean existsCardByNumber(String cardNumberHash);

    void saveAll(List<Card> cards);

}
