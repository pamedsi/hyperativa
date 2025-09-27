package com.hyperativa.card.infra;


import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class CardNumberEncryptor {

    @Qualifier("jasyptStringEncryptor")
    private final StringEncryptor encryptor;

    public CardNumberEncryptor(@Qualifier("jasyptStringEncryptor") StringEncryptor encryptor) {
        this.encryptor = encryptor;
    }

    public String encrypt(String cardNumber) {
        return encryptor.encrypt(cardNumber);
    }

}