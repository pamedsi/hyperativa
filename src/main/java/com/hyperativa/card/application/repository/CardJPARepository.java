package com.hyperativa.card.application.repository;


import com.hyperativa.card.domain.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardJPARepository extends JpaRepository<Card, Long> {

    boolean existsByCardNumberHash(String cardNumberHash);

}
