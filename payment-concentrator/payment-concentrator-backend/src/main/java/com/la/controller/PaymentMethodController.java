package com.la.controller;

import com.la.model.dtos.BuyerRequestDTO;
import com.la.model.dtos.PaymentMethodDTO;
import com.la.model.dtos.UrlDTO;
import com.la.model.mappers.PaymentMethodDTOMapper;
import com.la.security.TokenUtils;
import com.la.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/payment-method")
public class PaymentMethodController {

    @Autowired
    private PaymentMethodService paymentMethodService;

    @Autowired
    private PaymentMethodDTOMapper mapper;

    @Autowired
    private TokenUtils tokenUtils;

    @PostMapping("")
    @PreAuthorize("hasAuthority('CREATE_PAYMENT_METHOD')")
    public ResponseEntity<Long> createPaymentMethod(@RequestBody PaymentMethodDTO paymentMethodDTO) {
        try {
            return new ResponseEntity<>(paymentMethodService.createPaymentMethod(paymentMethodDTO), HttpStatus.CREATED);
        } catch (Exception e) {
            // e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('DELETE_PAYMENT_METHOD')")
    public ResponseEntity<Boolean> deletePaymentMethod(@PathVariable("id") Long paymentMethodId) {
        try {
            return new ResponseEntity<>(paymentMethodService.deletePaymentMethod(paymentMethodId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Return URL to LA to choose payment method
    @PostMapping(value = "subscriber/{token:.+}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('INITIATE_PAYMENT')")
    public ResponseEntity<UrlDTO> getPaymentMethodsUrl(@RequestBody BuyerRequestDTO buyerRequestDTO, @PathVariable String token) {
        try {
            String username = tokenUtils.getUsernameFromToken(token);
            return new ResponseEntity<>(new UrlDTO(paymentMethodService.getPaymentMethodsUrl(buyerRequestDTO, username)), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


}
