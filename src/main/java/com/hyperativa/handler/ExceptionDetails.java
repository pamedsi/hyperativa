package com.hyperativa.handler;


public record ExceptionDetails(
        String title,
        String message,
        int status,
        String timestamp,
        String path,
        String method
) {}
