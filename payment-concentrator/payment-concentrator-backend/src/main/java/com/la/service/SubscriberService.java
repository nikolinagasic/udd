package com.la.service;

import com.la.model.Subscriber;
import com.la.model.dtos.SubscriptionRequestDTO;

public interface SubscriberService {
    Subscriber getDetails(String username);
    boolean editDetails(Subscriber subscriber);
}
