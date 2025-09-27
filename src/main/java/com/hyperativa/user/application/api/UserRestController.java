package com.hyperativa.user.application.api;


import org.springframework.web.bind.annotation.RestController;

import com.hyperativa.auth.application.service.TokenService;
import com.hyperativa.user.application.service.UserService;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@RestController
public class UserRestController implements UserAPI {
    private final UserService userService;
    private final TokenService tokenService;

    @Override
    public void createUser(CreateUserRequest createUserRequest) {
        log.info("[starts] UserController -> createUser()");
        userService.createUser(createUserRequest);
        log.info("[ends] UserController -> createUser()\n");
    }

    @Override
    public void deleteUser(String token, UUID userIdentifier) {
        log.info("[starts] UserController -> deleteUser()");
        String email = tokenService.decode(token);
        userService.deleteUser(email, userIdentifier);
        log.info("[ends] UserController -> deleteUser()\n");
    }

}