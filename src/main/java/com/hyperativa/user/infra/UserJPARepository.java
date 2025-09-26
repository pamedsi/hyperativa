package com.hyperativa.user.infra;


import com.hyperativa.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserJPARepository extends JpaRepository <User, Long> {

    Optional<User> findByEmailAndDeletedAccountFalse(String email);

    Page<User> findAllByDeletedAccountFalse(Pageable pageable);

    Optional<User> findByIdentifierAndDeletedAccountFalse(UUID userIdentifier);

    boolean existsByEmailAndDeletedAccountFalse(String email);

}
