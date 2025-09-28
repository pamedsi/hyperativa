package com.hyperativa.user.domain;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.hyperativa.user.application.api.CreateUserRequest;

@Table(name = "users")
@Entity
@Getter
@RequiredArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter(AccessLevel.NONE)
    private Long id;
    @Column (unique = true)
    private UUID identifier;
    @Column
    private String name;
    @Column (unique = true)
    private String email;
    @Column
    @Enumerated (EnumType.STRING)
    private UserRole role;
    @Column
    private LocalDate birthdate;
    @Column
    private String passwordHash;
    @Column
    private Boolean deleted;

    public User(CreateUserRequest userRequestDTO, String hashedPassword) {
        identifier = UUID.randomUUID();
        name = userRequestDTO.name();
        email = userRequestDTO.email();
        passwordHash = hashedPassword;
        birthdate = userRequestDTO.getBirthdate();
        role = UserRole.CUSTOMER;
        deleted = false;
    }

    public void delete() {
        deleted = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        return List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @PrePersist
    @PreUpdate
    private void trimStrings() {
        if (name != null) name = name.trim();
        if (email != null) email = email.trim();
    }

    public boolean isAdmin() {
        return this.role == UserRole.ADMIN;
    }
}
