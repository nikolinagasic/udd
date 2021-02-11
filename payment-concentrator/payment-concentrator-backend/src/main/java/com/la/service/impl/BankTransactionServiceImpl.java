package com.la.service.impl;

import com.la.model.dtos.bank.BankRequestDTO;
import com.la.model.dtos.bank.BankResponseDTO;
import com.la.model.*;
import com.la.model.enums.Status;
import com.la.repository.*;
import com.la.service.BankTransactionService;
import com.la.service.CipherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BankTransactionServiceImpl implements BankTransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private BuyerRequestRepository buyerRequestRepository;

    @Autowired
    private CipherService cipherService;

    @Override
    public BankRequestDTO createBankRequestDTO(Long buyerRequestId) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException {
        Optional<BuyerRequest> buyerRequest = buyerRequestRepository.findById(buyerRequestId);
        if (buyerRequest.isPresent()){
            SubscriberDetails subscriberDetails = buyerRequest.get().getSubscriber().getSubscriberDetails();
            if(subscriberDetails != null){
                Transaction transaction = new Transaction();
                transaction.setStatus(Status.PENDING);
                transaction.setBuyerRequest(buyerRequest.get());
                transaction.setPaymentMethod(paymentMethodRepository.findByName("Bank"));
                transaction.setTimestamp(LocalDateTime.now());
                Transaction t = transactionRepository.save(transaction);

                return new BankRequestDTO(Long.parseLong(subscriberDetails.getMerchantId()),
                                        subscriberDetails.getMerchantPassword(),
                                        transaction.getId(),
                                        transaction.getTimestamp(),
                                        buyerRequest.get().getAmount());
            }
            return null;
        }
        return null;
    }

    @Override
    public void updateTransactionPaymentId(Long paymentId, Long buyerRequestId) {
        Transaction transaction = transactionRepository.findByBuyerRequest(buyerRequestRepository.findById(buyerRequestId).get());
        transaction.setPaymentId(paymentId);
        transactionRepository.save(transaction);
    }

    @Override
    public String updateTransactionError(Long buyerRequestId) {
        Transaction transaction = transactionRepository.findByBuyerRequest(buyerRequestRepository.findById(buyerRequestId).get());
        transaction.setStatus(Status.ERROR);
        transactionRepository.save(transaction);

        return transaction.getBuyerRequest().getSubscriber().getSubscriberDetails().getErrorUrl();
    }

    @Override
    public String updateTransaction(BankResponseDTO bankResponseDTO) {
        Transaction transaction = transactionRepository.findById(bankResponseDTO.getMerchantOrderId()).get();
        transaction.setAcqOrderId(String.valueOf(bankResponseDTO.getAcqOrderId()));
        transaction.setAcqTimestamp(bankResponseDTO.getAcqTimestamp());

        String returnUrl = "";

        System.err.println(bankResponseDTO.getStatus());

        switch (bankResponseDTO.getStatus()){
            case SUCCESS : {
                transaction.setStatus(Status.SUCCESS);
                returnUrl = transaction.getBuyerRequest().getSubscriber().getSubscriberDetails().getSuccessUrl() + "/" + transaction.getBuyerRequest().getMerchantOrderId();
                break;
            }
            case ERROR: {
                transaction.setStatus(Status.ERROR);
                returnUrl = transaction.getBuyerRequest().getSubscriber().getSubscriberDetails().getErrorUrl() + "/" + transaction.getBuyerRequest().getMerchantOrderId();
                break;
            }
            case FAILED: {
                transaction.setStatus(Status.FAILED);
                returnUrl = transaction.getBuyerRequest().getSubscriber().getSubscriberDetails().getFailedUrl() + "/" + transaction.getBuyerRequest().getMerchantOrderId();
                break;
            }
        }
        transactionRepository.save(transaction);

        return returnUrl;
    }
}
