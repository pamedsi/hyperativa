package com.hyperativa.user.infra;


import com.hyperativa.handler.exceptions.APIException;
import com.hyperativa.handler.exceptions.UserNotFoundException;
import com.hyperativa.user.application.repository.UserRepository;
import com.hyperativa.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Log4j2
@RequiredArgsConstructor
public class UserInfraRepository implements UserRepository {

    private final UserJPARepository userJPARepository;

    @Override
    public User getUserByEmail(String email) {
        log.info("[starts] UserInfraRepository - getUserByEmail()");
        User user = userJPARepository.findByEmailAndDeletedAccountFalse(email).orElseThrow(() -> new UserNotFoundException(email));
        log.info("[ends] UserInfraRepository - getUserByEmail()");
        return user;
    }

    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        log.info("[starts] UserInfraRepository - getAllUsers()");
        Page<User> users = userJPARepository.findAllByDeletedAccountFalse(pageable);
        log.info("[ends] UserInfraRepository - getAllUsers()");
        return users;
    }

    @Override
    public User getUserByIdentifier(UUID userIdentifier) {
        log.info("[starts] UserInfraRepository - getUserWithIdentifier()");
        User user = userJPARepository.findByIdentifierAndDeletedAccountFalse(userIdentifier).orElseThrow(() -> new APIException("Usuário não encontrado!", HttpStatus.NOT_FOUND));
        log.info("[ends] UserInfraRepository - getUserWithIdentifier()");
        return user;
    }

    @Override
    public boolean existsByEmail(String email) {
        log.info("[starts] UserInfraRepository - existsByEmail()");
        boolean exists = userJPARepository.existsByEmailAndDeletedAccountFalse(email);
        log.info("[ends] UserInfraRepository - existsByEmail()");
        return exists;
    }

    @Override
    public void saveUser(User user) {
        log.info("[starts] UserInfraRepository - saveUser()");
        userJPARepository.save(user);
        log.info("[ends] UserInfraRepository - saveUser()");
    }

}
