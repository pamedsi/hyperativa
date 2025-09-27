package com.hyperativa.handler.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@Getter
public class APIException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 3493924982910908850L;
	private final HttpStatus httpStatus;
	private final int httpStatusInNumber;

	public APIException(String message, HttpStatus status) {
		super(message);
		httpStatus = status;
		httpStatusInNumber = status.value();
	}

    public APIException() {
        super("Internal server error");
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        httpStatusInNumber = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

}
