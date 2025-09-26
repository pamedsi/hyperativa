package com.hyperativa.user.application.service;


import com.hyperativa.handler.exceptions.APIException;
import com.hyperativa.user.application.api.CreateUserRequest;
import com.hyperativa.user.application.api.UpdatePasswordRequest;
import com.hyperativa.user.application.api.UpdateUserRequest;
import com.hyperativa.user.application.api.UserDetailsDTO;
import com.hyperativa.user.application.repository.UserRepository;
import com.hyperativa.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserApplicationService implements UserService {
    private final UserRepository userRepository;

    @Override
    public void createUser(CreateUserRequest createUserRequestDTO) {
        log.info("[starts] UserApplicationService -> createUser()");
        User newUser = new User(createUserRequestDTO);
        userRepository.saveUser(newUser);
        log.info("[ends] UserApplicationService -> createUser()");
    }

    @Override
    public Page<UserDetailsDTO> getUsers(Pageable pageable) {
        log.info("[starts] UserApplicationService -> getUsers()");
        Page<User> users = userRepository.getAllUsers(pageable);
        Page<UserDetailsDTO> usersDTO = users.map(UserDetailsDTO::new);
        log.info("[ends] UserApplicationService -> getUsers()");
        return usersDTO;
    }

    @Override
    public UserDetailsDTO getUser(String email, UUID userIdentifier) {
        log.info("[starts] UserApplicationService -> getUser()");
        User user = verifyIfTheUserTryingToAccessCan(email, userIdentifier);
        UserDetailsDTO userDTO = new UserDetailsDTO(user);
        log.info("[ends] UserApplicationService -> getUser()");
        return userDTO;
    }

    @Override
    public void editUser(String email, UUID identifier, UpdateUserRequest userDTO) {
        log.info("[starts] UserApplicationService -> editUser()");
        User user = verifyIfTheUserTryingToAccessCan(email, identifier);
        user.updateUser(userDTO);
        userRepository.saveUser(user);
        log.info("[ends] UserApplicationService -> editUser()");
    }

    @Override
    public void deleteUser(String email, UUID userIdentifier) {
        log.info("[starts] UserApplicationService -> deleteUser()");
        User userToBeDeleted = verifyIfTheUserTryingToAccessCan(email, userIdentifier);
        userToBeDeleted.delete();
        userRepository.saveUser(userToBeDeleted);
        log.info("[ends] UserApplicationService -> deleteUser()");
    }

    @Override
    public void updatePassword(String email, UUID userIdentifier, UpdatePasswordRequest passwordRequest) {
        log.info("[starts] UserApplicationService -> updatePassword()");
        User user = verifyIfTheUserTryingToAccessCan(email, userIdentifier);
        user.updatePassword(passwordRequest);
        userRepository.saveUser(user);
        log.info("[ends] UserApplicationService -> updatePassword()");
    }

    private User verifyIfTheUserTryingToAccessCan(String email, UUID userToBeAccessedIdentifier) {
        User userWhoIsTrying = userRepository.getUserByEmail(email);
        User userToBeAccessed = userRepository.getUserByIdentifier(userToBeAccessedIdentifier);
        boolean userWhoIsTryingIsTheOneToBeAccessed = userToBeAccessed.equals(userWhoIsTrying);
        if (userWhoIsTrying.isAdmin() || userWhoIsTryingIsTheOneToBeAccessed) return userToBeAccessed;
        throw new APIException("Você não tem autorização para isso", HttpStatus.FORBIDDEN);
    }
}
