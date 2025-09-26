package com.hyperativa.auth.application.api;


import com.hyperativa.user.domain.UserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record ValidTokenResponse(
        String name,
        @Enumerated(EnumType.STRING)
        UserRole role
) {}
