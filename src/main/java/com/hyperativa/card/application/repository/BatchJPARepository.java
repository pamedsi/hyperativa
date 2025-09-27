package com.hyperativa.card.application.repository;

import com.hyperativa.card.domain.Batch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchJPARepository extends JpaRepository<Batch, Long> {
}
