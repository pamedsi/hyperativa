package com.hyperativa.user.application.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Optional;

public record UpdateUserRequest (
        String name,
        String email,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate birthDate,
        String CPF,
        @Size(max = 11, min = 10, message = "Número de celular com DDD é maior que 11 caracteres")
        Optional<String> phoneNumber
) {}