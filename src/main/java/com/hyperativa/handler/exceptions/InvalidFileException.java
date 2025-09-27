package com.hyperativa.handler.exceptions;


import org.springframework.http.HttpStatus;

public class InvalidFileException extends APIException {

    public InvalidFileException() {
        super("Your text file is invalid. Check if it obeys the requirements", HttpStatus.BAD_REQUEST);
    }

    public InvalidFileException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

}
