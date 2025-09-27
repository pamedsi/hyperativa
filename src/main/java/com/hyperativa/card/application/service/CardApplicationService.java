package com.hyperativa.card.application.service;


import com.hyperativa.card.application.api.CardNumber;
import com.hyperativa.card.domain.Card;
import com.hyperativa.card.application.repository.CardRepository;
import com.hyperativa.card.infra.CardNumberEncryptor;
import com.hyperativa.user.application.repository.UserRepository;
import com.hyperativa.user.domain.User;
import com.hyperativa.utils.Hasher;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;


@Log4j2
@Service
@RequiredArgsConstructor
public class CardApplicationService implements CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final Hasher hasher;
    private final CardNumberEncryptor encryptor;

    @Override
    public void addSingleCard(CardNumber cardNumber, String ownerCardEmail) {
        log.info("[starts]: CardApplicationService.addSingleCard()");
        User owner = userRepository.getUserByEmail(ownerCardEmail);
        String hashCardNumber = hasher.hashCardNumber(cardNumber.cardNumber());
        String encryptedCardNumber = encryptor.encrypt(cardNumber.cardNumber());
        Card newCard = new Card(owner, hashCardNumber, encryptedCardNumber);
        cardRepository.save(newCard);
        log.info("[ends]: CardApplicationService.addSingleCard()");
    }

}
