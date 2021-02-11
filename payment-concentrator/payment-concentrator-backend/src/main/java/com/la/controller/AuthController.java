package com.la.controller;

import com.la.model.dtos.PaymentMethodDTO;
import com.la.model.dtos.SubscriptionRequestDTO;
import com.la.model.mappers.PaymentMethodDTOMapper;
import com.la.security.UserTokenState;
import com.la.security.auth.JwtAuthenticationRequest;
import com.la.service.AuthService;
import com.la.service.PaymentMethodService;
import com.la.service.SubscriptionRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private SubscriptionRequestService subscriptionService;

    @Autowired
    private PaymentMethodService paymentMethodService;

    @Autowired
    private PaymentMethodDTOMapper mapper;

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody JwtAuthenticationRequest authenticationRequest) {
        System.err.println(authenticationRequest);
        try {
            UserTokenState userTokenState = authService.login(authenticationRequest);
            if (userTokenState == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(userTokenState, HttpStatus.OK);
        } catch (UsernameNotFoundException unfe) {
            return new ResponseEntity<>("Deleted user.", HttpStatus.BAD_REQUEST);
        } catch (BadCredentialsException bce) {
            return new ResponseEntity<>("Wrong username or password.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/subscribe")
    public ResponseEntity<Long> createRequest(@Valid @RequestBody SubscriptionRequestDTO requestDTO) {
        try {
            Long id = subscriptionService.createRequest(requestDTO);
            return new ResponseEntity<>(id, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/payment-method")
    public ResponseEntity<List<PaymentMethodDTO>> getAll() {
        try {
            return new ResponseEntity<>(paymentMethodService.getAll().stream().map(mapper::toDto)
                    .collect(Collectors.toList()), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/payment-method/other")
    public ResponseEntity<List<PaymentMethodDTO>> getAllWithoutFirstThree() {
        try {
            return new ResponseEntity<>(paymentMethodService.getAllWithoutFirstThree().stream().map(mapper::toDto)
                    .collect(Collectors.toList()), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
