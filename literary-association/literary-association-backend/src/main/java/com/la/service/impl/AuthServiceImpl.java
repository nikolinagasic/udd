package com.la.service.impl;

import com.la.model.users.Role;
import com.la.model.users.SysUser;
import com.la.security.TokenUtils;
import com.la.security.UserTokenState;
import com.la.security.auth.JwtAuthenticationRequest;
import com.la.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenUtils tokenUtils;

    @Override
    public UserTokenState login(JwtAuthenticationRequest authenticationRequest) throws Exception {
        final Authentication authentication = authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        SysUser sysUser = (SysUser) authentication.getPrincipal();
        if (!sysUser.isActive()) {
            throw new Exception();
        }
        List<Role> roles = new ArrayList<>(sysUser.getRoles());
        String token = tokenUtils.generateToken(sysUser.getUsername(), roles);
        int expiresIn = tokenUtils.getExpiredIn();

        return new UserTokenState(token, expiresIn, roles.stream().map(Role::getName).collect(Collectors.toList()));
    }
}
