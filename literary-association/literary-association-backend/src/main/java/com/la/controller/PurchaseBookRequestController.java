package com.la.controller;

import com.la.model.dtos.CreatedBookPurchaseRequestDTO;
import com.la.model.dtos.PurchaseBookRequestDTO;
import com.la.model.enums.TransactionStatus;
import com.la.service.PurchaseBookRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/auth/purchase-book")
public class PurchaseBookRequestController {

    @Autowired
    private PurchaseBookRequestService purchaseBookRequestService;

    @PostMapping(value = "")
    public ResponseEntity<?> createPurchaseRequest(@RequestBody PurchaseBookRequestDTO purchaseBookRequestDTO) {
        try {
            CreatedBookPurchaseRequestDTO purchaseDTO = purchaseBookRequestService.createPurchaseRequest(purchaseBookRequestDTO);
            return new ResponseEntity<>(purchaseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/update/{transactionId}")
    public ResponseEntity<?> updateRequestStatus(@RequestBody String transactionStatus, @PathVariable Long transactionId) {
        try {
            boolean success = purchaseBookRequestService.updateRequest(transactionId, transactionStatus);
            if (success) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
