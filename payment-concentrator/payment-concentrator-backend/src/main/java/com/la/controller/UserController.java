package com.la.controller;

import com.la.model.Subscriber;
import com.la.model.SubscriberDetails;
import com.la.model.dtos.SubscriptionRequestDTO;
import com.la.model.dtos.UserDTO;
import com.la.security.TokenUtils;
import com.la.service.SubscriberService;
import com.la.service.UserService;
import com.la.service.impl.AuthServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private TokenUtils tokenUtils;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("")
    @PreAuthorize("hasAuthority('VIEW_USERS')")
    public ResponseEntity<List<UserDTO>> view() {
        try {
            return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/activate/{id}")
    @PreAuthorize("hasAuthority('UPDATE_USER')")
    public ResponseEntity<Boolean> activate(@PathVariable("id") Long userId) {
        try {
            logger.info("Date : {}, Login API / " + "User = " + userId +  " / Method : activate() /" +
                    " Warn : {}.", LocalDateTime.now(), "User account activated.");

            return new ResponseEntity<>(userService.activate(userId), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deactivate/{id}")
    @PreAuthorize("hasAuthority('UPDATE_USER')")
    public ResponseEntity<Boolean> deactivate(@PathVariable("id") Long userId) {
        try {
            logger.info("Date : {}, Login API / " + "User = " + userId +  " / Method : deactivate() /" +
                    " Warn : {}.", LocalDateTime.now(), "User account deactivated.");

            return new ResponseEntity<Boolean>(userService.deactivate(userId), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/details/{token:.+}")
    @PreAuthorize("hasAuthority('VIEW_USER_DETAILS')")
    public ResponseEntity<Subscriber> getDetails(@PathVariable String token) {
        try {
            System.err.println("USAO U DETAILS");
            return new ResponseEntity<>(subscriberService.getDetails(tokenUtils.getUsernameFromToken(token)), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/edit/details/{token:.+}")
    @PreAuthorize("hasAuthority('EDIT_USER_DETAILS')")
    public ResponseEntity<?> editDetails(@RequestBody Subscriber subscriber, @PathVariable String token) {
        try {
            return new ResponseEntity<>(subscriberService.editDetails(subscriber), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
