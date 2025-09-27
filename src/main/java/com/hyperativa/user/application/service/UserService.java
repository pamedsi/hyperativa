package com.hyperativa.user.application.service;


import com.hyperativa.user.application.api.CreateUserRequest;

import java.util.UUID;

public interface UserService {

    void createUser(CreateUserRequest createUserRequest);

    void deleteUser(String email, UUID userIdentifier);

}
