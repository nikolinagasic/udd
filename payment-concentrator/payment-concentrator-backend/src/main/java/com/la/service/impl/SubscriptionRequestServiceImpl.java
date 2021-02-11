package com.la.service.impl;

import com.la.model.SubscriberDetails;
import com.la.model.dtos.PaymentMethodDTO;
import com.la.model.dtos.SubscriptionRequestDTO;
import com.la.model.mappers.SubscriptionRequestDTOMapper;
import com.la.model.PaymentMethod;
import com.la.model.Subscriber;
import com.la.model.SubscriptionRequest;
import com.la.repository.RoleRepository;
import com.la.repository.SubscriberDetailsRepository;
import com.la.repository.SubscriptionRequestRepository;
import com.la.repository.UserRepository;
import com.la.service.CipherService;
import com.la.service.SubscriptionRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Array;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class SubscriptionRequestServiceImpl implements SubscriptionRequestService {

    @Autowired
    private SubscriptionRequestRepository subscriptionRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SubscriptionRequestDTOMapper mapper;

    @Autowired
    private SubscriberDetailsRepository subscriberDetailsRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private CipherService cipherService;

    @Override
    public List<SubscriptionRequest> getAll() {
        return subscriptionRequestRepository.findAll();
    }

    @Override
    public Long createRequest(SubscriptionRequestDTO requestDTO) throws ParseException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException {
        SubscriptionRequest newRequest = new SubscriptionRequest();

        newRequest.setOrganizationDescription(requestDTO.getOrganizationDescription());
        newRequest.setOrganizationEmail(requestDTO.getOrganizationEmail());
        newRequest.setOrganizationName(requestDTO.getOrganizationName());

        newRequest.setPassword(encoder.encode(requestDTO.getPassword()));
        newRequest.setUsername(requestDTO.getUsername());

        Set<PaymentMethod> paymentMethods = new HashSet<>();
        for (PaymentMethodDTO dto : requestDTO.getPaymentMethods()) {
            PaymentMethod method = new PaymentMethod(dto.getId(), dto.getName());
            if (method.getName().equals("Bank")){
                newRequest.setMerchantId(cipherService.encrypt(requestDTO.getMerchantId()));
                newRequest.setMerchantPassword(cipherService.encrypt(requestDTO.getMerchantPassword()));
            }
            else if (method.getName().equals("PayPal")){
                newRequest.setClientId(cipherService.encrypt(requestDTO.getClientId()));
                newRequest.setClientSecret(cipherService.encrypt(requestDTO.getClientSecret()));
            }
            else if (method.getName().equals("Bitcoin")){
                newRequest.setBitcoinToken(cipherService.encrypt(requestDTO.getBitcoinToken()));
            }
            paymentMethods.add(method);
        }
        newRequest.setPaymentMethods(paymentMethods);

        newRequest.setErrorUrl(requestDTO.getErrorUrl());
        newRequest.setFailedUrl(requestDTO.getFailedUrl());
        newRequest.setSuccessUrl(requestDTO.getSuccessUrl());
        return (subscriptionRequestRepository.saveAndFlush(newRequest)).getId();
    }

    @Override
    public void approveRequest(Long requestId) {
        SubscriptionRequest request = subscriptionRequestRepository.getOne(requestId);
        Set<PaymentMethod> m = new HashSet<>(request.getPaymentMethods());

        // TO DO: send mail

        Subscriber newSubscriber = new Subscriber(request.getUsername(),
                request.getPassword(), request.getOrganizationEmail(), m);
        newSubscriber.setRoles(new HashSet<>(Collections.singletonList(roleRepository.findByName("ROLE_SUBSCRIBER"))));

        SubscriberDetails subscriberDetails = new SubscriberDetails();
        subscriberDetails.setErrorUrl(request.getErrorUrl());
        subscriberDetails.setSuccessUrl(request.getSuccessUrl());
        subscriberDetails.setFailedUrl(request.getFailedUrl());
        subscriberDetails.setClientId(request.getClientId());
        subscriberDetails.setClientSecret(request.getClientSecret());
        subscriberDetails.setBitcoinToken(request.getBitcoinToken());
        subscriberDetails.setMerchantId(request.getMerchantId());
        subscriberDetails.setMerchantPassword(request.getMerchantPassword());
        subscriberDetails.setOrganizationDescription(request.getOrganizationDescription());
        subscriberDetails.setOrganizationName(request.getOrganizationName());
        subscriberDetails.setOrganizationEmail(request.getOrganizationEmail());

        subscriberDetails = subscriberDetailsRepository.save(subscriberDetails);
        newSubscriber.setSubscriberDetails(subscriberDetails);

        newSubscriber.setCreatedAt(LocalDateTime.now());
        newSubscriber.setUpdatedAt(LocalDateTime.now());
        newSubscriber.setActive(true);

        userRepository.save(newSubscriber);

        subscriptionRequestRepository.deleteById(requestId);
    }

    @Override
    public Boolean declineRequest(Long requestId) {
        try {
            subscriptionRequestRepository.deleteById(requestId);
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }
}
