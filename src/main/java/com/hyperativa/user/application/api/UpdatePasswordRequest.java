package com.hyperativa.user.application.api;

import jakarta.validation.constraints.NotBlank;

public record UpdatePasswordRequest(
        @NotBlank
        String newPassword
) {}
