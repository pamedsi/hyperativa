package com.hyperativa.card.application.service;


public record BatchHeader(
        String name,
        String date,
        String batch,
        int amount,
        String fileName
) {}
