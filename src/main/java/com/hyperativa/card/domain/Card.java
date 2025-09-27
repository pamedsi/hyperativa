package com.hyperativa.card.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.hyperativa.user.domain.User;

@Entity
@Table(name = "cards")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter(AccessLevel.NONE)
    private Long id;

    @Getter
    @Column (unique = true, nullable = false)
    private UUID identifier;

    @Column (unique = true, nullable = false)
    private String cardNumberHash;

    @Column(nullable = false)
    private String encryptedCardNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    private Batch batch;

    @Column(nullable = false)
    private LocalDateTime savedAt;

    public Card(User owner, String hashedCardNumber, String encryptedCardNumber) {
        this.identifier = UUID.randomUUID();
        this.cardNumberHash = hashedCardNumber;
        this.encryptedCardNumber = encryptedCardNumber;
        this.owner = owner;
        this.savedAt = LocalDateTime.now();
    }

}

