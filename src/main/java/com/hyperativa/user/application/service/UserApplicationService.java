package com.hyperativa.user.application.service;


import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.UUID;

import com.hyperativa.handler.exceptions.ActionNotAllowedException;
import com.hyperativa.user.application.api.CreateUserRequest;
import com.hyperativa.user.application.repository.UserRepository;
import com.hyperativa.user.domain.User;
import com.hyperativa.utils.Hasher;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserApplicationService implements UserService {
    private final UserRepository userRepository;
    private final Hasher hasher;

    @Override
    public void createUser(CreateUserRequest createUserRequestDTO) {
        log.info("[starts] UserApplicationService -> createUser()");
        String hashedPassword = hasher.hashPassword(createUserRequestDTO.password());
        User newUser = new User(createUserRequestDTO, hashedPassword);
        userRepository.saveUser(newUser);
        log.info("[ends] UserApplicationService -> createUser()");
    }

    @Override
    public void deleteUser(String email, UUID userIdentifier) {
        log.info("[starts] UserApplicationService -> deleteUser()");
        User userToBeDeleted = verifyIfTheUserTryingToAccessCan(email, userIdentifier);
        userToBeDeleted.delete();
        userRepository.saveUser(userToBeDeleted);
        log.info("[ends] UserApplicationService -> deleteUser()");
    }

    private User verifyIfTheUserTryingToAccessCan(String email, UUID userToBeAccessedIdentifier) {
        User userWhoIsTrying = userRepository.getUserByEmail(email);
        User userToBeAccessed = userRepository.getUserByIdentifier(userToBeAccessedIdentifier);
        boolean userWhoIsTryingIsTheOneToBeAccessed = userToBeAccessed.equals(userWhoIsTrying);
        if (userWhoIsTrying.isAdmin() || userWhoIsTryingIsTheOneToBeAccessed) return userToBeAccessed;
        throw new ActionNotAllowedException("You don't have permission to delete this user");
    }

}
