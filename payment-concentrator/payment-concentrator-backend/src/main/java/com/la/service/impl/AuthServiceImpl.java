package com.la.service.impl;

import com.la.model.Role;
import com.la.model.User;
import com.la.repository.UserRepository;
import com.la.security.TokenUtils;
import com.la.security.UserTokenState;
import com.la.security.auth.JwtAuthenticationRequest;
import com.la.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private UserRepository<User> userRepository;

    Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    public UserTokenState login(JwtAuthenticationRequest authenticationRequest) {

        if (userRepository.findByUsername(authenticationRequest.getUsername()) != null) {
            User user = userRepository.findByUsername(authenticationRequest.getUsername());

            if (user.getAttempts() == 6) {
                user.setActive(false);
                userRepository.save(user);

                logger.warn("Date : {}, Login API / " + "User = " + user.getUsername() +  " / Method : login() /" +
                        " Warn : {}.", LocalDateTime.now(), "User account locked. Allowed number of attempts reached.");

                return null;
            }
            else {
                user.setAttempts(user.getAttempts() + 1);
                userRepository.save(user);
            }
        }

        final Authentication authentication = authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        System.err.println(user);

        List<Role> roles = new ArrayList<>(user.getRoles());
        String token = tokenUtils.generateToken(user.getUsername(), roles);
        int expiresIn = tokenUtils.getExpiredIn();

        UserTokenState userTokenState = new UserTokenState(token, expiresIn, roles.stream().map(Role::getName).collect(Collectors.toList()));

        if (userTokenState != null) {
            user.setLastLogin(LocalDateTime.now());
            user.setAttempts(0);
            userRepository.save(user);

            if (!user.getActive()) {
                return null;
            }
        }
        else {
            logger.warn("Date : {}, Login API / " + "User = " + user.getUsername() +  " / Method : login() /" +
                    " Warn : {}.", LocalDateTime.now(), "Login failed.");
        }

        return userTokenState;
    }
}
