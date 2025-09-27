package com.hyperativa.card.application.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import com.hyperativa.card.domain.Card;
import com.hyperativa.user.domain.User;

public interface CardJPARepository extends JpaRepository<Card, Long> {

    boolean existsByCardNumberHash(String cardNumberHash);

    Optional<Card> findByOwnerAndCardNumberHash(User owner, String hashCardNumber);

}
