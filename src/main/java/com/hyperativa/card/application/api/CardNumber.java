package com.hyperativa.card.application.api;


import com.hyperativa.card.infra.ValidCardNumber;

public record CardNumber(
        @ValidCardNumber
        String cardNumber
) {}
