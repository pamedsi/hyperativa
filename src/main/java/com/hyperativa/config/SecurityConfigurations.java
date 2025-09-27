package com.hyperativa.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfigurations {

    private final SecurityFilter securityFilter;
    private static final String USER_ENDPOINT = "/user";
    private static final String CUSTOMER = "CUSTOMER";
    private static final String ADMIN = "ADMIN";

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain setFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests( authorize -> authorize
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // Authentication:
                        .requestMatchers(HttpMethod.POST, "/authentication/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/authentication/validation").permitAll()

                        // Users:
                        .requestMatchers(HttpMethod.GET, USER_ENDPOINT).hasRole(ADMIN)
                        .requestMatchers(HttpMethod.GET, USER_ENDPOINT +"/{identifier}").hasRole(CUSTOMER)
                        .requestMatchers(HttpMethod.POST, USER_ENDPOINT).permitAll()
                        .requestMatchers(HttpMethod.PUT, USER_ENDPOINT).hasRole(CUSTOMER)
                        .requestMatchers(HttpMethod.DELETE, USER_ENDPOINT).hasRole(CUSTOMER)
                        .requestMatchers(HttpMethod.PATCH, USER_ENDPOINT + "/password").hasRole(CUSTOMER)

                        // Cards:
                        .requestMatchers(HttpMethod.POST, USER_ENDPOINT + "/card/single").hasRole(CUSTOMER)

                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder();}

}
