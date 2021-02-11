package com.la.repository;

import com.la.model.PurchaseBookRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseBookRequestRepository extends JpaRepository<PurchaseBookRequest, Long> {
}
