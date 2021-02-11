package com.la.repository;

import com.la.model.BuyerRequest;
import com.la.model.PaymentMethod;
import com.la.model.enums.Status;
import com.la.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findByAcqOrderId(String orderId);

    Transaction findByBuyerRequest(BuyerRequest buyerRequest);

    Transaction findByPaymentId(Long paymentId);

    List<Transaction> findByStatus(Status status);

    List<Transaction> findByStatusAndPaymentMethod(Status status, PaymentMethod paymentMethod);
}
