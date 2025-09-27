package com.hyperativa.card.application.repository;


import com.hyperativa.card.domain.Batch;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchRepository {

    Batch saveAndGet(Batch batch);

    void save(Batch batch);

}
