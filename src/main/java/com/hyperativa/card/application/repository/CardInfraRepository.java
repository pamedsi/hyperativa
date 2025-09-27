package com.hyperativa.card.application.repository;


import com.hyperativa.card.domain.Card;
import com.hyperativa.handler.exceptions.APIException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

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

}
