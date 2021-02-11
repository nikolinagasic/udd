package com.la.service.impl;

import com.la.model.Subscriber;
import com.la.model.SubscriberDetails;
import com.la.model.User;
import com.la.model.dtos.SubscriptionRequestDTO;
import com.la.repository.SubscriberDetailsRepository;
import com.la.repository.SubscriberRepository;
import com.la.repository.UserRepository;
import com.la.service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriberServiceImpl implements SubscriberService {

    @Autowired
    private SubscriberDetailsRepository subscriberDetailsRepository;

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Override
    public Subscriber getDetails(String username) {
        // Kako vratiti password?
        return subscriberRepository.findByUsername(username);
    }

    @Override
    public boolean editDetails(Subscriber subscriber) {
        // Treba ubaciti enkripciju podataka
        return false;
    }
}
