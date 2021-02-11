package com.la.repository;

import com.la.model.BuyerRequest;
import com.la.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuyerRequestRepository extends JpaRepository<BuyerRequest, Long> {
    List<BuyerRequest> findBySubscriber(Subscriber byUsername);
}
