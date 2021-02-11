package com.la.service;

import com.la.model.dtos.bitcoin.OrderRequest;
import com.la.model.dtos.bitcoin.OrderResult;
import com.la.model.enums.Status;

public interface BitcoinTransactionService {

    OrderRequest createOrderRequest(Long buyerRequestId);

    Status updateTransaction(OrderResult orderResult);
}
