package com.hyperativa.user.infra;


import com.hyperativa.user.application.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if (email == null || email.isBlank()) {
            return false;
        }

        try {
            return !userRepository.existsByEmail(email);
        }
        catch (Exception e) {
            return false;
        }
    }

}
