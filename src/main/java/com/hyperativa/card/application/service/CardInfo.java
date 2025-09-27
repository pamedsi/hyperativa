package com.hyperativa.card.application.service;

public record CardInfo(
        String batchNumber,
        String cardNumber,
        int lineNumber
) {}
