package com.hyperativa.user.application.api;


import com.hyperativa.auth.application.service.TokenService;
import com.hyperativa.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@RestController
public class UserController implements UserAPI {
    private final UserService userService;
    private final TokenService tokenService;

    @Override
    public void createUser(CreateUserRequest createUserRequest) {
        log.info("[starts] UserController -> createUser()");
        userService.createUser(createUserRequest);
        log.info("[ends] UserController -> createUser()");
    }

    @Override
    public Page<UserDetailsDTO> getUsers(Pageable pageable) {
        log.info("[starts] UserController -> getUsers()");
        Page<UserDetailsDTO> page = userService.getUsers(pageable);
        log.info("[ends] UserController -> getUsers()");
        return page;
    }

    @Override
    public UserDetailsDTO getUser(String token, UUID userIdentifier) {
        log.info("[starts] UserController -> getUser()");
        String email = tokenService.decode(token);
        UserDetailsDTO user = userService.getUser(email, userIdentifier);
        log.info("[ends] UserController -> getUser()");
        return user;
    }

    @Override
    public void editUser(String token, UUID userIdentifier, UpdateUserRequest userDTO) {
        log.info("[starts] UserController -> editUser()");
        String email = tokenService.decode(token);
        userService.editUser(email, userIdentifier, userDTO);
        log.info("[ends] UserController -> editUser()");
    }

    @Override
    public void deleteUser(String token, UUID userIdentifier) {
        log.info("[starts] UserController -> deleteUser()");
        String email = tokenService.decode(token);
        userService.deleteUser(email, userIdentifier);
        log.info("[ends] UserController -> deleteUser()");
    }

    @Override
    public void updatePassword(String token, UUID userIdentifier, UpdatePasswordRequest passwordRequest) {
        log.info("[starts] UserController -> updatePassword()");
        String email = tokenService.decode(token);
        userService.updatePassword(email, userIdentifier, passwordRequest);
        log.info("[ends] UserController -> updatePassword()");
    }
}