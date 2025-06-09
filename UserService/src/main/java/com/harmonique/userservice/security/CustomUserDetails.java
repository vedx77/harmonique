package com.harmonique.userservice.security;

import com.harmonique.userservice.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * This class is a custom implementation of Spring Security's UserDetails interface.
 * It wraps around the application's User entity and provides authentication-related details.
 */
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;

    private final User user;

    /**
     * Returns the authorities (roles) granted to the user.
     * Spring Security uses these to authorize requests.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles()
                .stream()
                .map(role -> (GrantedAuthority) () -> role.getName()) // Convert role name to GrantedAuthority
                .collect(Collectors.toList());
    }

    /**
     * Returns the user's hashed password used for authentication.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Returns the username used for authentication (here, email is used as username).
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Indicates whether the user's account has expired. True means the account is valid.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user's account is locked. True means the account is not locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) have expired. True means credentials are valid.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled. True means the user is allowed to authenticate.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}