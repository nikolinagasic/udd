package com.la.service;

import com.la.security.UserTokenState;
import com.la.security.auth.JwtAuthenticationRequest;

public interface AuthService {
    UserTokenState login(JwtAuthenticationRequest authenticationRequest) throws Exception;
}
