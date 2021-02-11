package com.la.service;

import com.la.model.dtos.bank.BankRequestDTO;
import com.la.model.dtos.bank.BankResponseDTO;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface BankTransactionService {

    BankRequestDTO createBankRequestDTO(Long paymentId) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException;

    String updateTransaction(BankResponseDTO bankResponseDTO);

    String updateTransactionError(Long buyerRequestId);

    void updateTransactionPaymentId(Long paymentId, Long buyerRequestId);

}
