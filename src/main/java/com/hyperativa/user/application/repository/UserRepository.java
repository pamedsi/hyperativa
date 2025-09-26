package com.hyperativa.user.application.repository;

import com.hyperativa.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository {

    void saveUser(User user);

    Optional<User> findUserByEmail(String email);

    User getUserByEmail(String email);

    Page<User> getAllUsers(Pageable pageable);

    User getUserByIdentifier(UUID userIdentifier);

    boolean existsByEmail(String email);
}
