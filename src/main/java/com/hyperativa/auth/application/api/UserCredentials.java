package com.hyperativa.auth.application.api;

import jakarta.validation.constraints.NotBlank;

public record UserCredentials(
        @NotBlank
        String login,
        @NotBlank
        String password
) {}