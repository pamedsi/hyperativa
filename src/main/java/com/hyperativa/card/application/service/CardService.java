package com.hyperativa.card.application.service;


import com.hyperativa.card.application.api.CardNumber;
import org.springframework.stereotype.Service;

@Service
public interface CardService {

    void addSingleCard(CardNumber cardNumber, String ownerCardEmail);

}
