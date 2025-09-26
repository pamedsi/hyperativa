package com.hyperativa.handler.exceptions;

import org.springframework.http.HttpStatus;

import static java.lang.String.format;

public class UserNotFoundException extends APIException {

    public UserNotFoundException(String email) {
        super(format("User with email %s was not found", email), HttpStatus.NOT_FOUND);
    }

}
