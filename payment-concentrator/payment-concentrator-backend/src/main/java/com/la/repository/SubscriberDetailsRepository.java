package com.la.repository;

import com.la.model.SubscriberDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberDetailsRepository extends JpaRepository<SubscriberDetails, Long> {

    SubscriberDetails findByMerchantId(Long merchantId);
}
