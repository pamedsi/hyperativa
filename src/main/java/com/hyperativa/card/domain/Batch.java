package com.hyperativa.card.domain;


import com.hyperativa.card.application.service.BatchHeader;
import com.hyperativa.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Batch {
    @Id
    @Getter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private LocalDateTime processedAt;

    @Column(nullable = false)
    private Integer totalRecords;

    private Integer successfulRecords;
    private Integer failedRecords;

    @Enumerated(EnumType.STRING)
    private BatchStatus status;

    private String name;
    private String date;
    private String batchNumber;

    @ManyToOne
    private User processedBy;

    @OneToMany(mappedBy = "batch")
    private final List<Card> cards = new ArrayList<>();

    @Setter
    private String errorMessage;

    public Batch(BatchHeader batchHeader, User processedBy) {
        this.filename = batchHeader.fileName();
        this.processedAt = LocalDateTime.now();
        this.totalRecords = batchHeader.amount();
        this.processedBy = processedBy;
        this.status = BatchStatus.PROCESSING;
        this.successfulRecords = 0;
        this.failedRecords = 0;
        this.name = batchHeader.name();
        this.date = batchHeader.date();
        this.batchNumber = batchHeader.batch();
    }

    public void markAsCompleted() {
        this.status = BatchStatus.COMPLETED;
        this.processedAt = LocalDateTime.now();
    }

    public void markAsFailed() {
        this.status = BatchStatus.FAILED;
        this.processedAt = LocalDateTime.now();
    }

    public void markAsPartialSuccess(int successful, int failed) {
        this.status = BatchStatus.PARTIAL_SUCCESS;
        this.successfulRecords = successful;
        this.failedRecords = failed;
        this.processedAt = LocalDateTime.now();
    }

}