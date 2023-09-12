package com.viesonet.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class PhoneNumberAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final String phoneNumber;

    public PhoneNumberAuthenticationToken(Object principal, Object credentials, String phoneNumber,
            Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
