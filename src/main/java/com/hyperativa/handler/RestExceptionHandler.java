package com.hyperativa.handler;


import com.hyperativa.handler.exceptions.APIException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Log4j2
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDetails> handleValidationExceptions(MethodArgumentNotValidException exception, HttpServletRequest request) {
        log.error(ExceptionDetails.getExceptionTitle(exception.getClass().toString()));
        String errorMessage = exception.getBindingResult().getAllErrors().getFirst().getDefaultMessage();
        log.error(errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionDetails(
                        "Bad Request",
                        errorMessage,
                        400,
                        LocalDateTime.now().toString(),
                        request.getServletPath(),
                        request.getMethod()
                ));
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ExceptionDetails> handleAuthenticationExceptions(InternalAuthenticationServiceException exception, HttpServletRequest request) {
        log.error(ExceptionDetails.getExceptionTitle(exception.getClass().toString()));
        log.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ExceptionDetails(
                        "Unauthorized",
                        "Email or password incorrect!",
                        401,
                        LocalDateTime.now().toString(),
                        request.getServletPath(),
                        request.getMethod()
                ));
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<ExceptionDetails> apiExceptionHandler(APIException exception, HttpServletRequest request) {
        log.error(ExceptionDetails.getExceptionTitle(exception.getClass().toString()));
        log.error(exception.getMessage());
        return ResponseEntity.status(exception.getHttpStatus()).body(
                new ExceptionDetails(
                        "Unexpected Error",
                        exception.getMessage(),
                        exception.getHttpStatusInNumber(),
                        LocalDateTime.now().toString(),
                        request.getServletPath(),
                        request.getMethod()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDetails> generalException(Exception exception, HttpServletRequest request) {
        log.error(ExceptionDetails.getExceptionTitle(exception.getClass().toString()));
        log.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ExceptionDetails(
                        "Internal Server Error",
                        "Internal error! Please contact support or try again later.",
                        500,
                        LocalDateTime.now().toString(),
                        request.getServletPath(),
                        request.getMethod()
                ));
    }

}