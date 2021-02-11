package com.la.repository;

import com.la.model.SubscriptionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRequestRepository extends JpaRepository<SubscriptionRequest, Long> {
}
