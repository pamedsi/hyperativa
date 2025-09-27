package com.hyperativa.card.infra;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import com.hyperativa.card.application.repository.CardJPARepository;
import com.hyperativa.card.application.repository.CardRepository;
import com.hyperativa.card.domain.Card;
import com.hyperativa.handler.exceptions.APIException;
import com.hyperativa.handler.exceptions.CardNotFoundException;
import com.hyperativa.user.domain.User;

@Log4j2
@Repository
@RequiredArgsConstructor
public class CardInfraRepository implements CardRepository {

    private final CardJPARepository cardJPARepository;

    @Override
    public void save(Card newCard) {
        log.info("[starts]: CardApplicationService.save()");
        try {
            cardJPARepository.save(newCard);
        }
        catch (IllegalArgumentException | OptimisticLockingFailureException e ) {
            log.error(e.getMessage());
            throw new APIException("Error saving card!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("[ends]: CardApplicationService.save()");
    }

    @Override
    public boolean existsCardByNumber(String cardNumberHash) {
        log.info("[starts]: CardApplicationService.existsCardByNumber()");
        try {
            return cardJPARepository.existsByCardNumberHash(cardNumberHash);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }
        log.info("[ends]: CardApplicationService.existsCardByNumber()");
        return false;
    }

    @Override
    public void saveAll(List<Card> cards) {
        log.info("[starts]: CardApplicationService.saveAll()");
        try {
            cardJPARepository.saveAll(cards);
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            throw new APIException("One of the card numbers already exists!", HttpStatus.CONFLICT);
        }
        log.info("[ends]: CardApplicationService.saveAll()");
    }

    @Override
    public Card getCardByOwner(User owner, String hashCardNumber) {
        log.info("[starts]: CardApplicationService.getCardByOwner()");
        Card card = cardJPARepository.findByOwnerAndCardNumberHash(owner, hashCardNumber)
                .orElseThrow(CardNotFoundException::new);
        log.info("[ends]: CardApplicationService.getCardByOwner()");
        return card;
    }

}
