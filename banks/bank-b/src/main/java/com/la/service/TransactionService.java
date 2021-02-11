package com.la.service;

import com.la.model.dtos.*;
import com.la.model.Payment;

public interface TransactionService {

    BankPaymentUrlDTO createPayment(BankRequestDTO bankRequestDTO);

    BankResponseDTO createTransaction(TransactionFormDataDTO transactionFormDataDTO, Long paymentId);

    Payment getPayment(Long paymentId);

    PCCResponseDTO createSecondTransaction(PCCRequestDTO pccRequestDTO);
}
