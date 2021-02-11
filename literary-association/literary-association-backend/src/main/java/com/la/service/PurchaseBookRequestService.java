package com.la.service;

import com.la.model.dtos.CreatedBookPurchaseRequestDTO;
import com.la.model.dtos.PurchaseBookRequestDTO;
import com.la.model.enums.TransactionStatus;

import java.text.ParseException;

public interface PurchaseBookRequestService {
    CreatedBookPurchaseRequestDTO createPurchaseRequest(PurchaseBookRequestDTO purchaseBookRequestDTO) throws ParseException;
    boolean updateRequest(Long transactionId, String transactionStatus);
}
