package com.viesonet.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.viesonet.entity.Accounts;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private String id;
    private String username;
    private String email;
    private String password;
    private int role; // Đặt là một chuỗi để biểu diễn vai trò.

    public UserDetailsImpl(String id, String username, String email, String password, int role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static UserDetailsImpl build(Accounts user) {
        System.out.println("role la: " + user.getRole().getRoleId());
        return new UserDetailsImpl(
                user.getPhoneNumber(),
                user.getUser().getUserId(),
                user.getEmail(),
                user.getPassword(),
                user.getRole().getRoleId());
    }

    public String getId() {
        return id;
    }

    public int getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Sử dụng SimpleGrantedAuthority để biểu diễn vai trò của người dùng.
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
}
