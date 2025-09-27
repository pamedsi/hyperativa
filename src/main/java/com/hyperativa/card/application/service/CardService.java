package com.hyperativa.card.application.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hyperativa.card.application.api.CardId;
import com.hyperativa.card.application.api.CardNumber;

@Service
public interface CardService {

    void addSingleCard(CardNumber cardNumber, String ownerCardEmail);

    void addBatchCards(MultipartFile file, String ownerCardEmail);

    CardId getCardInfo(String cardNumber, String ownerEmail);

}
