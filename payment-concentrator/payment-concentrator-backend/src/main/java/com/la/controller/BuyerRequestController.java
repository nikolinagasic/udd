package com.la.controller;

import com.la.model.dtos.PaymentMethodsBuyerRequestDTO;
import com.la.service.BuyerRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/buyer-request")
public class BuyerRequestController {

    @Autowired
    private BuyerRequestService buyerRequestService;

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodsBuyerRequestDTO> get(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(buyerRequestService.get(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
