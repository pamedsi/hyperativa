package com.hyperativa.user.application.repository;

import com.hyperativa.user.domain.User;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository {

    void saveUser(User user);

    User getUserByEmail(String email);

    User getUserByIdentifier(UUID userIdentifier);

    boolean existsByEmail(String email);

}
