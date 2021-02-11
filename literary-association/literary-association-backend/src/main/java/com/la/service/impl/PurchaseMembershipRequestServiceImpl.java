package com.la.service.impl;

import com.la.model.MembershipRequest;
import com.la.model.dtos.PurchaseMembershipRequestDTO;
import com.la.model.enums.TransactionStatus;
import com.la.repository.MembershipRepository;
import com.la.repository.MembershipRequestRepository;
import com.la.repository.ReaderRepository;
import com.la.security.TokenUtils;
import com.la.service.PurchaseMembershipRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PurchaseMembershipRequestServiceImpl implements PurchaseMembershipRequestService {

    @Autowired
    private MembershipRequestRepository membershipRequestRepository;

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private TokenUtils tokenUtils;

    @Override
    public PurchaseMembershipRequestDTO createMembershipRequest(Long membership_id, String token) {
        MembershipRequest request = new MembershipRequest();
        request.setStatus(TransactionStatus.WAITING_PAYMENT);
        request.setReader(readerRepository.findByUsername("reader"));
        membershipRequestRepository.saveAndFlush(request);

        PurchaseMembershipRequestDTO dto = new PurchaseMembershipRequestDTO();
        dto.setOrderId(request.getId());
        if (membership_id.equals(1L)) {
            dto.setAmount(10d);
        } else if (membership_id.equals(2L)) {
            dto.setAmount(30d);
        } else {
            dto.setAmount(50d);
        }
        dto.setUsername(tokenUtils.getUsernameFromToken(token.substring(7)));
        return dto;
    }
}
