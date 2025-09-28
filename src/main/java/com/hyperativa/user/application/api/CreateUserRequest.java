package com.hyperativa.user.application.api;


import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
        @Schema(example = "12/25/1990", description = "Date in MM/DD/YYYY format")
        @JsonFormat(pattern = "MM/dd/yyyy")
        @ValidBirthdate
        String birthdate
) {

        public LocalDate getBirthdate() {
            return LocalDate.parse(birthdate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        }

}
