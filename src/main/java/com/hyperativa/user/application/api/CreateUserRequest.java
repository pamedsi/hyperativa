package com.hyperativa.user.application.api;


import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;

import java.time.LocalDate;

import com.hyperativa.user.infra.ValidBirthdate;
import com.hyperativa.user.infra.ValidEmail;
import com.hyperativa.user.infra.ValidPassword;
import com.hyperativa.user.infra.ValidUserName;

public record CreateUserRequest(
        @ValidUserName
        String name,
        @Email
        @ValidEmail
        String email,
        @ValidPassword
        String password,
        @JsonFormat(pattern = "dd/MM/yyyy")
        @ValidBirthdate
        LocalDate birthdate
) {}
