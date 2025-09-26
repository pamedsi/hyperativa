package com.hyperativa.user.application.service;


import com.hyperativa.user.application.api.CreateUserRequest;
import com.hyperativa.user.application.api.UpdatePasswordRequest;
import com.hyperativa.user.application.api.UpdateUserRequest;
import com.hyperativa.user.application.api.UserDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {

    void createUser(CreateUserRequest createUserRequest);

    Page<UserDetailsDTO> getUsers(Pageable pageable);

    UserDetailsDTO getUser(String email, UUID userIdentifier);

    void editUser(String email, UUID identifier, UpdateUserRequest userDTO);

    void deleteUser(String email, UUID userIdentifier);

    void updatePassword(String email, UUID userIdentifier, UpdatePasswordRequest passwordRequest);

}
