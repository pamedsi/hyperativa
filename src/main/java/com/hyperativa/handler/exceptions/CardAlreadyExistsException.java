package com.hyperativa.handler.exceptions;

import org.springframework.http.HttpStatus;

import static java.lang.String.format;

public class CardAlreadyExistsException extends APIException {

    public CardAlreadyExistsException(String cardNumber) {
        super(format("Card number %s already exists: ", cardNumber), HttpStatus.CONFLICT);
    }

}
