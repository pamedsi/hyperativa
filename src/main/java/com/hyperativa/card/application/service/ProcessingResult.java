package com.hyperativa.card.application.service;

public record ProcessingResult(
   int successCount,
    int failureCount
) {}
