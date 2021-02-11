package com.la.service.scheduling;

import com.la.model.dtos.bitcoin.OrderResult;
import com.la.model.enums.Status;
import com.la.model.Transaction;
import com.la.repository.PaymentMethodRepository;
import com.la.repository.TransactionRepository;
import com.la.service.BitcoinTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Component
public class UpdateOrderStatus {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    PaymentMethodRepository paymentMethodRepository;

    @Autowired
    BitcoinTransactionService bitcoinTransactionService;

    @Autowired
    RestTemplate restTemplate;

    Logger logger = LoggerFactory.getLogger(UpdateOrderStatus.class);
    
    @Scheduled(fixedRate = 86400000)
    public void checkOrderStatus() {
        List<Transaction> transactionList = transactionRepository.findByStatusAndPaymentMethod(Status.PENDING, paymentMethodRepository.findByName("Bitcoin"));
        OrderResult orderResult;
        for (Transaction transaction : transactionList){
            orderResult = getOrder(Long.parseLong(transaction.getAcqOrderId()));
            if (orderResult != null){
                Status status = bitcoinTransactionService.updateTransaction(orderResult);

                switch (status) {
                    case PENDING: {
                        logger.info("Date : {}, Bitcoin API / " + "TransactionId = " + transaction.getId() + " / Method : checkOrderStatus() /" +
                                " Info : {}.", LocalDateTime.now(), "Bitcoin API returned new transaction status. Transaction status : PENDING.");
                        break;
                    }
                    case FAILED: {
                        logger.error("Date : {}, Bitcoin API / " + "TransactionId = " + transaction.getId() + " / Method : checkOrderStatus() /" +
                                " Error : {}.", LocalDateTime.now(), "Bitcoin API returned new transaction status. Transaction status : FAILED.");
                        break;
                    }
                    case SUCCESS: {
                        logger.info("Date : {}, Bitcoin API / " + "TransactionId = " + transaction.getId() + " / Method : checkOrderStatus() /" +
                                " Info : {}.", LocalDateTime.now(), "Bitcoin API returned new transaction status. Transaction status : SUCCESS.");
                        break;
                    }
                    case ERROR: {
                        logger.error("Date : {}, Bitcoin API / " + "TransactionId = " + transaction.getId() + " / Method : checkOrderStatus() /" +
                                " Error : {}.", LocalDateTime.now(), "Bitcoin API returned new transaction status. Transaction status : ERROR.");
                        break;
                    }
                    default: {
                        logger.warn("Date : {}, Bitcoin API / " + "TransactionId = " + transaction.getId() + " / Method : checkOrderStatus() /" +
                                " Warn : {}.", LocalDateTime.now(), "Bitcoin API returned new transaction status. Transaction status : UNDEFINED.");
                        break;
                    }
                }
            }
            else {
                logger.error("Date : {}, Bitcoin API / " + "TransactionId = " + transaction.getId() + " / Method : checkOrderStatus() /" +
                        " Error : {}.", LocalDateTime.now(), "Bitcoin API didn't return result. Transaction status : UNDEFINED.");
            }
        }
    }

    private OrderResult getOrder(Long orderId){
        OrderResult orderResult = null;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            orderResult = restTemplate.exchange("https://bitcoin/order/" + orderId,
                    HttpMethod.GET, null, new ParameterizedTypeReference<OrderResult>() {}).getBody();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return orderResult;
    }
}
