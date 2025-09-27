package com.hyperativa.user.application.api;


import com.hyperativa.user.domain.User;
import com.hyperativa.user.domain.UserRole;

import java.util.UUID;

public record UserDetailsDTO (
        UUID identifier,
        String name,
        String email,
        UserRole role
) {

    public UserDetailsDTO (User userEntity) {
        this(
                userEntity.getIdentifier(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getRole()
        );
    }

}
