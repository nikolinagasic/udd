package com.la.controller;

import com.la.model.dtos.TransactionDTO;
import com.la.model.dtos.UserDTO;
import com.la.security.TokenUtils;
import com.la.service.TransactionService;
import com.la.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TokenUtils tokenUtils;

    @GetMapping("")
    @PreAuthorize("hasAuthority('VIEW_ALL_TRANSACTIONS')")
    public ResponseEntity<List<TransactionDTO>> view() {
        try {
            return new ResponseEntity<>(transactionService.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{token:.+}")
    @PreAuthorize("hasAuthority('VIEW_USER_TRANSACTIONS')")
    public ResponseEntity<List<TransactionDTO>> viewByUser(@PathVariable String token) {
        try {
            return new ResponseEntity<>(transactionService.findByUser(tokenUtils.getUsernameFromToken(token)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
