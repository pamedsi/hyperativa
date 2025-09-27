package com.hyperativa.utils;


import lombok.Getter;
import org.apache.commons.codec.digest.DigestUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Hasher {

    private final String fixedSalt;

    public Hasher(@Value("${env.salt}") String salt) {
        this.fixedSalt = salt;
    }

    public String hashCardNumber(String data) {
        String dataWithSalt = data + fixedSalt;
        return DigestUtils.sha256Hex(dataWithSalt);
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

}
