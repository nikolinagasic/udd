package com.la.repository;

import com.la.model.Subscriber;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberRepository extends UserRepository<Subscriber> {
    Subscriber findByUsername(String username);
}
