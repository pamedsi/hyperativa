package com.hyperativa.card.infra;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static java.lang.String.format;


@Component
public class ValidTextFileValidator implements ConstraintValidator<ValidTextFile, MultipartFile> {

    private long maxSize;
    private String[] allowedTypes;

    @Override
    public void initialize(ValidTextFile constraintAnnotation) {
        this.maxSize = constraintAnnotation.maxSize();
        this.allowedTypes = constraintAnnotation.allowedTypes();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            context.buildConstraintViolationWithTemplate("Text file cannot be null or empty")
                    .addConstraintViolation();
            return false;
        }

        if (file.getSize() > maxSize) {
            context.buildConstraintViolationWithTemplate(
                            format("File too big, maximum size is %s", maxSize))
                    .addConstraintViolation();
            return false;
        }

        if (!isValidContentType(file.getContentType())) {
            context.buildConstraintViolationWithTemplate(
                            format("File type not allowed, types allowed are: %s",
                                    Arrays.toString(allowedTypes)))
                    .addConstraintViolation();
            return false;
        }

        if (!isReadableText(file)) {
            context.buildConstraintViolationWithTemplate("File is not a valid readable text")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

    private boolean isValidContentType(String contentType) {
        if (contentType == null) return false;

        return Arrays.stream(allowedTypes)
                .anyMatch(allowed -> contentType.equals(allowed) ||
                        contentType.startsWith(allowed + "/"));
    }

    private boolean isReadableText(MultipartFile file) {
        try {
            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            return !content.trim().isEmpty();
        } catch (IOException e) {
            return false;
        }
    }

}