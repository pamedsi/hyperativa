package com.hyperativa.card.infra;


import com.hyperativa.card.application.repository.BatchJPARepository;
import com.hyperativa.card.application.repository.BatchRepository;
import com.hyperativa.card.domain.Batch;
import com.hyperativa.handler.exceptions.APIException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
@RequiredArgsConstructor
public class BatchInfraRepository implements BatchRepository {

    private final BatchJPARepository batchJPARepository;

    @Override
    public Batch saveAndGet(Batch batch) {
        log.info("[starts]: BatchInfraRepository.saveAndGet()");
        try {
            batch = batchJPARepository.saveAndFlush(batch);
        }
        catch (Exception e) {
            log.error("Error saving and getting batch: {}", e.getMessage());
            throw new APIException();
        }
        log.info("[ends]: BatchInfraRepository.saveAndGet()");
        return batch;
    }

    @Override
    public void save(Batch batch) {
        log.info("[starts]: BatchInfraRepository.save()");
        try {
            batchJPARepository.save(batch);
        }
        catch (Exception e) {
            log.error("Error saving batch: {}", e.getMessage());
            throw new APIException();
        }
        log.info("[ends]: BatchInfraRepository.save()");

    }

}
