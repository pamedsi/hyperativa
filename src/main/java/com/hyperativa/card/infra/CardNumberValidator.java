package com.hyperativa.card.infra;


import com.hyperativa.card.application.repository.CardRepository;
import com.hyperativa.utils.Hasher;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import static com.hyperativa.handler.CustomMessageBuilder.buildCustomMessage;
import static java.lang.String.format;

@RequiredArgsConstructor
public class CardNumberValidator implements ConstraintValidator<ValidCardNumber, String> {

    public static final int MIN_LENGTH = 13;
    public static final int MAX_LENGTH = 19;
    private final CardRepository cardRepository;
    private final Hasher hasher;

    @Override
    public boolean isValid(String cardNumber, ConstraintValidatorContext context) {

        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            buildCustomMessage(context, "Card number cannot be null or empty");
            return false;
        }

        String cleanedNumber = cardNumber.replaceAll("\\s+", "");
        if (cleanedNumber.length() < MIN_LENGTH || cleanedNumber.length() > MAX_LENGTH) {
            buildCustomMessage(context, format("Card number must be at least %s and maximum %s length", MIN_LENGTH, MAX_LENGTH));
            return false;
        }

        if (!cleanedNumber.matches("\\d+")) {
            buildCustomMessage(context, "Card number must contain only digits");
            return false;
        }

        if (!isValidLuhn(cleanedNumber)) {
            buildCustomMessage(context, format("'%s' is not a valid card number", cardNumber));
            return false;
        }

        String hashCardNumber = hasher.hashCardNumber(cardNumber);
        if (cardRepository.existsCardByNumber(hashCardNumber)) {
            buildCustomMessage(context, "Card number already exists");
            return false;
        }

        return true;
    }



    private boolean isValidLuhn(String number) {
        int sum = 0;
        boolean doubleDigit = false;

        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(number.charAt(i));

            if (doubleDigit) {
                digit *= 2;
                if (digit > 9) {
                    digit = digit - 9;
                }
            }

            sum += digit;
            doubleDigit = !doubleDigit;
        }

        return (sum % 10 == 0);
    }

}
