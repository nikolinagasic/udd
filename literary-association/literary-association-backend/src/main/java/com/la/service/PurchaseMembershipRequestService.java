package com.la.service;

import com.la.model.dtos.PurchaseMembershipRequestDTO;

public interface PurchaseMembershipRequestService {
    PurchaseMembershipRequestDTO createMembershipRequest(Long membership_id, String token);
}
