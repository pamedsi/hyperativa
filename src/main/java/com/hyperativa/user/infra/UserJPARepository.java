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

    Optional<User> findByEmailAndDeletedFalse(String email);

    Page<User> findAllByDeletedFalse(Pageable pageable);

    Optional<User> findByIdentifierAndDeletedFalse(UUID userIdentifier);

    boolean existsByEmailAndDeletedFalse(String email);

}
