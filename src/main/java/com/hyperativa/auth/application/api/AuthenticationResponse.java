package com.hyperativa.auth.application.api;


import com.hyperativa.user.domain.UserRole;

public record AuthenticationResponse(
        String token,
        UserRole role
) {}
