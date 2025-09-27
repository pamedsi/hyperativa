package com.hyperativa.card.application.service;


import com.hyperativa.card.application.api.CardNumber;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface CardService {

    void addSingleCard(CardNumber cardNumber, String ownerCardEmail);

    void addBatchCards(MultipartFile file, String ownerCardEmail);

}
