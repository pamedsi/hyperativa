package com.hyperativa.handler.exceptions;


import org.springframework.http.HttpStatus;

public class JwtException extends APIException {

    public JwtException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
