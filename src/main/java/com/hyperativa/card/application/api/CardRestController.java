package com.hyperativa.card.application.api;


import com.hyperativa.auth.application.service.TokenService;
import com.hyperativa.card.application.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@RestController
@RequiredArgsConstructor
public class CardRestController implements CardAPI {

    private final CardService cardService;
    private final TokenService tokenService;

    @Override
    public void addSingleCard(CardNumber cardNumber, String token) {
        log.info("[starts]: CardRestController.addSingleCard()");
        String ownerCardEmail = tokenService.decode(token);
        cardService.addSingleCard(cardNumber, ownerCardEmail);
        log.info("[ends]: CardRestController.addSingleCard()\n");
    }

    @Override
    public void addBatchCards(MultipartFile file, String token) {
        log.info("[starts]: CardRestController.addBatchCards()");
        String ownerCardEmail = tokenService.decode(token);
        cardService.addBatchCards(file, ownerCardEmail);
        log.info("[ends]: CardRestController.addBatchCards()\n");
    }

}
