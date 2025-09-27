package com.hyperativa.user.application.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;

import jakarta.validation.Valid;

import java.util.UUID;

@RequestMapping("/user")
public interface UserAPI {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void createUser(@Valid @RequestBody CreateUserRequest createUserRequest);

    @DeleteMapping("/{identifier}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void deleteUser(@RequestHeader("Authorization") String token, @PathVariable("identifier") UUID userIdentifier);

}
