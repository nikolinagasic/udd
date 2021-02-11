package com.la.service.impl;

import com.la.model.BuyerRequest;
import com.la.model.Transaction;
import com.la.model.dtos.TransactionDTO;
import com.la.repository.BuyerRequestRepository;
import com.la.repository.SubscriberRepository;
import com.la.repository.TransactionRepository;
import com.la.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BuyerRequestRepository buyerRequestRepository;

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Override
    public List<TransactionDTO> getAll() {
        List<Transaction> transactions = transactionRepository.findAll();
        List<TransactionDTO> transactionDTOs = new ArrayList<>();
        for(Transaction transaction : transactions) {
            TransactionDTO transactionDTO = new TransactionDTO();
            transactionDTO.setUsername(transaction.getBuyerRequest().getSubscriber().getUsername());
            transactionDTO.setTimestamp(transaction.getTimestamp().toString());
            transactionDTO.setAmount(transaction.getBuyerRequest().getAmount());
            transactionDTO.setAcqOrderId(transaction.getAcqOrderId());
            transactionDTO.setAcqTimestamp(transaction.getAcqTimestamp().toString());
            transactionDTO.setMerchantOrderId(transaction.getBuyerRequest().getMerchantOrderId().toString());
            transactionDTO.setMerchantTimestamp(transaction.getBuyerRequest().getMerchantTimestamp().toString());
            transactionDTO.setStatus(transaction.getStatus().toString());
            transactionDTO.setPaymentMethod(transaction.getPaymentMethod().getName());
            transactionDTOs.add(transactionDTO);
        }

        return transactionDTOs;
    }

    @Override
    public List<TransactionDTO> findByUser(String username) {
        List<BuyerRequest> buyerRequests = buyerRequestRepository.findBySubscriber((subscriberRepository.findByUsername(username)));
        List<TransactionDTO> transactionDTOs = new ArrayList<>();

        for (BuyerRequest b : buyerRequests) {
            Transaction transaction = transactionRepository.findByBuyerRequest(b);
            TransactionDTO transactionDTO = new TransactionDTO();
            transactionDTO.setUsername(transaction.getBuyerRequest().getSubscriber().getUsername());
            transactionDTO.setTimestamp(transaction.getTimestamp().toString());
            transactionDTO.setAmount(transaction.getBuyerRequest().getAmount());
            transactionDTO.setAcqOrderId(transaction.getAcqOrderId());
            transactionDTO.setAcqTimestamp(transaction.getAcqTimestamp().toString());
            transactionDTO.setMerchantOrderId(transaction.getBuyerRequest().getMerchantOrderId().toString());
            transactionDTO.setMerchantTimestamp(transaction.getBuyerRequest().getMerchantTimestamp().toString());
            transactionDTO.setStatus(transaction.getStatus().toString());
            transactionDTO.setPaymentMethod(transaction.getPaymentMethod().getName());
            transactionDTOs.add(transactionDTO);
        }

        return transactionDTOs;
    }
}
