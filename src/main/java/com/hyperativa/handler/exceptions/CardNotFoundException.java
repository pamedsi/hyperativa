package com.hyperativa.handler.exceptions;

import org.springframework.http.HttpStatus;

public class CardNotFoundException extends APIException {

    public CardNotFoundException() {
        super("Card not found!", HttpStatus.NOT_FOUND);
    }

}
