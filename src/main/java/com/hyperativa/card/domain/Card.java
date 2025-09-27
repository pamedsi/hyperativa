package com.hyperativa.card.domain;


import com.hyperativa.user.domain.User;
import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cards")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter(AccessLevel.NONE)
    private Long id;

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

