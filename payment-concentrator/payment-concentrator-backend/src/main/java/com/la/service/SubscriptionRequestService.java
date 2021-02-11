package com.la.service;

import com.la.model.dtos.SubscriptionRequestDTO;
import com.la.model.SubscriptionRequest;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.List;

public interface SubscriptionRequestService {
    List<SubscriptionRequest> getAll();

    Long createRequest(SubscriptionRequestDTO requestDTO) throws ParseException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException;

    void approveRequest(Long requestId);

    Boolean declineRequest(Long requestId);
}
