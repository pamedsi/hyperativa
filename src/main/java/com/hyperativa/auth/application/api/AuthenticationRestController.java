package com.hyperativa.auth.application.api;


import com.hyperativa.auth.application.service.TokenService;
import com.hyperativa.user.application.repository.UserRepository;
import com.hyperativa.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequiredArgsConstructor
public class AuthenticationRestController implements AuthenticationAPI {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Override
    public AuthenticationResponse login(UserCredentials userCredentials) {
        log.info("[starts] AuthenticationController -> login()");
        UsernamePasswordAuthenticationToken username = new UsernamePasswordAuthenticationToken(userCredentials.login(), userCredentials.password());
        Authentication auth = authenticationManager.authenticate(username);
        User user = (User) auth.getPrincipal();
        String token = tokenService.generateToken(user);
        log.info("[ends] AuthenticationController -> login()\n");
        return new AuthenticationResponse(token, user.getRole());
    }

    @Override
    public ValidTokenResponse validateToken(String token) {
        log.info("[starts] AuthenticationController -> validateToken()");
        String email = tokenService.decode(token);
        User user = userRepository.getUserByEmail(email);
        log.info("[ends] AuthenticationController -> validateToken()\n");
        return new ValidTokenResponse(user.getName(), user.getRole());
    }

}
