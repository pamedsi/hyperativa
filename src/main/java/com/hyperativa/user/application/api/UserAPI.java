package com.hyperativa.user.application.api;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/user")
public interface UserAPI {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void createUser(@Valid @RequestBody CreateUserRequest createUserRequest);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<UserDetailsDTO> getUsers(@PageableDefault(direction = Sort.Direction.ASC, sort = { "name" }) Pageable pageable);

    @GetMapping("/{userIdentifier}")
    @ResponseStatus(HttpStatus.OK)
    UserDetailsDTO getUser(@RequestHeader("Authorization") String token, @PathVariable UUID userIdentifier);

    @PutMapping("/{identifier}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void editUser(@RequestHeader("Authorization") String token, @PathVariable("identifier") UUID identifier, @RequestBody UpdateUserRequest userDTO);

    @PatchMapping ("/password/{identifier}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void updatePassword(@RequestHeader("Authorization") String token, @PathVariable("identifier") UUID identifier, @RequestBody UpdatePasswordRequest passwordRequest);

    @DeleteMapping("/{identifier}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void deleteUser(@RequestHeader("Authorization") String token, @PathVariable("identifier") UUID userIdentifier);

}
