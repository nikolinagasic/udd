package com.la.controller;

import com.la.model.dtos.PurchaseMembershipRequestDTO;
import com.la.service.PurchaseMembershipRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/auth/purchase-membership")
public class PurchaseSubscriptionRequestController {

    @Autowired
    private PurchaseMembershipRequestService purchaseMembershipRequestService;

    @PostMapping(value = "/{membership_id}")
    public ResponseEntity<?> createSubscriptionRequest(@PathVariable Long membership_id,
                                                       @RequestHeader("Authorization") String token) {
        try {
            PurchaseMembershipRequestDTO dto = purchaseMembershipRequestService.createMembershipRequest(membership_id, token);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
