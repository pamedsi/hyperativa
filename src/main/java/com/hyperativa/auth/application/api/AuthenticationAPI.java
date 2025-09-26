package com.hyperativa.auth.application.api;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/authentication")
public interface AuthenticationAPI {

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    AuthenticationResponse login(@RequestBody @Valid UserCredentials userCredentials);

    @GetMapping("/validation")
    ValidTokenResponse validateToken (@RequestHeader("Authorization") String token);

}