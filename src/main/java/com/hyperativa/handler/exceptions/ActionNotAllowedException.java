package com.hyperativa.handler.exceptions;

import org.springframework.http.HttpStatus;

public class ActionNotAllowedException extends  APIException {

    public ActionNotAllowedException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }

}
