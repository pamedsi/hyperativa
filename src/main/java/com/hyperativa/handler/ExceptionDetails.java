package com.hyperativa.handler;


import java.util.List;

public record ExceptionDetails(
        String title,
        String message,
        int status,
        String timestamp,
        String path,
        String method
) {

    public static String getExceptionTitle(String classExceptionName) {
        String exceptionTitle = List.of(classExceptionName.split("\\.")).getLast().replace("Exception", "");
        return exceptionTitle.replaceAll("(?<=[a-z])(?=[A-Z])", " ");
    }

}
