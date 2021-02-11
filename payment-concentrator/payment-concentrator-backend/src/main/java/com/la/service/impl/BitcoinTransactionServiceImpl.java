package com.la.service.impl;

import com.la.model.dtos.bitcoin.OrderRequest;
import com.la.model.dtos.bitcoin.OrderResult;
import com.la.model.BuyerRequest;
import com.la.model.enums.Status;
import com.la.model.Transaction;
import com.la.repository.BuyerRequestRepository;
import com.la.repository.PaymentMethodRepository;
import com.la.repository.TransactionRepository;
import com.la.service.BitcoinTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class BitcoinTransactionServiceImpl implements BitcoinTransactionService {

    @Autowired
    private BuyerRequestRepository buyerRequestRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Override
    public OrderRequest createOrderRequest(Long buyerRequestId) {

        if (buyerRequestRepository.findById(buyerRequestId).isPresent()){
            BuyerRequest buyerRequest = buyerRequestRepository.findById(buyerRequestId).get();

            Long transactionId = createTransaction(buyerRequest);

            OrderRequest orderRequest = new OrderRequest();
            orderRequest.setOrder_id(transactionId.toString());
            orderRequest.setPrice_currency("USD");
            orderRequest.setReceive_currency("BTC");
            orderRequest.setPrice_amount((double) buyerRequest.getAmount());
            orderRequest.setSuccess_url(buyerRequest.getSubscriber().getSubscriberDetails().getSuccessUrl() + "/" + buyerRequest.getMerchantOrderId());
            orderRequest.setCancel_url(buyerRequest.getSubscriber().getSubscriberDetails().getFailedUrl() + "/" + buyerRequest.getMerchantOrderId());

            return orderRequest;
        }

        return null;
    }

    @Override
    public Status updateTransaction(OrderResult orderResult) {
        if (transactionRepository.findById(Long.parseLong(orderResult.getOrder_id())).isPresent()) {
            Transaction transaction = transactionRepository.findById(Long.parseLong(orderResult.getOrder_id())).get();

            switch (orderResult.getStatus()){
                case ("pending") :
                case ("new") :
                case ("confirming") : {
                    transaction.setStatus(Status.PENDING);
                    break;
                }
                case ("paid") : {
                    transaction.setStatus(Status.SUCCESS);
                    break;
                }
                default : {
                    transaction.setStatus(Status.FAILED);
                    break;
                }
            }
            transaction.setAcqTimestamp(LocalDateTime.parse(orderResult.getCreated_at(), DateTimeFormatter.ISO_ZONED_DATE_TIME));
            transaction.setAcqOrderId(String.valueOf(orderResult.getId()));
            transaction = transactionRepository.save(transaction);
            return  transaction.getStatus();
        }
        return Status.ERROR;
    }

    private Long createTransaction(BuyerRequest buyerRequest){

        Transaction transaction = new Transaction();
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setStatus(Status.PENDING);
        transaction.setBuyerRequest(buyerRequest);
        transaction.setPaymentMethod(paymentMethodRepository.findByName("Bitcoin"));
        transaction = transactionRepository.save(transaction);

        return transaction.getId();
    }
}
