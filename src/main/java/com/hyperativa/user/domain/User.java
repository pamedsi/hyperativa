package com.hyperativa.user.domain;


import com.hyperativa.user.application.api.CreateUserRequest;
import com.hyperativa.user.application.api.UpdatePasswordRequest;
import com.hyperativa.user.application.api.UpdateUserRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Table (name = "user_entity")
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
    @Getter(AccessLevel.NONE)
    private Boolean deletedAccount;

    public User(CreateUserRequest userRequestDTO) {
        identifier = UUID.randomUUID();
        name = userRequestDTO.name();
        email = userRequestDTO.email();
        passwordHash = hashPassword(userRequestDTO.password());
        birthdate = userRequestDTO.birthdate();
        role = UserRole.CUSTOMER;
        deletedAccount = false;
    }

    public void updatePassword(UpdatePasswordRequest newPassword) {
        passwordHash = hashPassword(newPassword.newPassword());
    }

    public void delete() {
        deletedAccount = true;
    }

    private String hashPassword(String password) {return BCrypt.hashpw(password, BCrypt.gensalt());}

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

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @PrePersist
    @PreUpdate
    private void trimStrings() {
        if (name != null) name = name.trim();
        if (email != null) email = email.trim();
    }

    public void updateUser(UpdateUserRequest userDTO) {
        name = Objects.requireNonNullElse(userDTO.name(), name);
        email = Objects.requireNonNullElse(userDTO.email(), email);
    }

    public boolean isAdmin() {
        return this.role == UserRole.ADMIN;
    }
}
