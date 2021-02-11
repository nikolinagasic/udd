package com.la.controller;

import com.la.model.dtos.UrlDTO;
import com.la.model.dtos.bitcoin.OrderRequest;
import com.la.model.dtos.bitcoin.OrderResult;
import com.la.model.enums.Status;
import com.la.service.BitcoinTransactionService;
import com.la.service.BuyerRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collections;

@RestController
@RequestMapping(value = "/api/auth/bitcoin/transaction")
public class BitcoinTransactionController {

    @Autowired
    private BitcoinTransactionService bitcoinTransactionService;

    @Autowired
    private BuyerRequestService buyerRequestService;

    @Autowired
    private RestTemplate restTemplate;

    Logger logger = LoggerFactory.getLogger(BitcoinTransactionController.class);

    @GetMapping(value="/{buyerRequestId}")
    public ResponseEntity<UrlDTO> getBitcoinPaymentUrl(@PathVariable Long buyerRequestId){
        UrlDTO urlDTO = new UrlDTO();
        try{
            OrderRequest orderRequest = bitcoinTransactionService.createOrderRequest(buyerRequestId);

            if (orderRequest != null) {
                OrderResult orderResult = getBitcoinPaymentUrl(orderRequest);

                if (orderResult != null) {
                    Status status = bitcoinTransactionService.updateTransaction(orderResult);

                    switch (status) {
                        case PENDING: {
                            logger.info("Date : {}, Bitcoin API / " + "BuyerRequestId = " + buyerRequestId +  " / Method : getBitcoinPaymentUrl() /" +
                                    " Info : {}.", LocalDateTime.now(), "Created new transaction. Transaction status : PENDING.");
                            break;
                        }
                        case FAILED: {
                            urlDTO.setUrl(buyerRequestService.getFailedUrl(buyerRequestId));
                            logger.error("Date : {}, Bitcoin API / " + "BuyerRequestId = " + buyerRequestId +  " / Method : getBitcoinPaymentUrl() /" +
                                    " Error : {}.", LocalDateTime.now(), "Created new transaction. Transaction status : FAILED.");
                            break;
                        }
                        case SUCCESS: {
                            logger.info("Date : {}, Bitcoin API / " + "BuyerRequestId = " + buyerRequestId +  " / Method : getBitcoinPaymentUrl() /" +
                                    " Info : {}.", LocalDateTime.now(), "Created new transaction. Transaction status : SUCCESS.");
                            break;
                        }
                        case ERROR: {
                            urlDTO.setUrl(buyerRequestService.getErrorUrl(buyerRequestId));
                            logger.error("Date : {}, Bitcoin API / " + "BuyerRequestId = " + buyerRequestId +  " / Method : getBitcoinPaymentUrl() /" +
                                    " Error : {}.", LocalDateTime.now(), "Created new transaction. Transaction status : ERROR.");
                            break;
                        }
                        default: {
                            urlDTO.setUrl(buyerRequestService.getErrorUrl(buyerRequestId));
                            logger.warn("Date : {}, Bitcoin API / " + "BuyerRequestId = " + buyerRequestId +  " / Method : getBitcoinPaymentUrl() /" +
                                    " Warn : {}.", LocalDateTime.now(), "Created new transaction. Transaction status : UNDEFINED.");
                            break;
                        }
                    }

                    if (orderResult.getPayment_url() != null) {
                        urlDTO.setUrl(orderResult.getPayment_url());

                        logger.info("Date : {}, Bitcoin API / " + "BuyerRequestId = " + buyerRequestId + " / Method : getBitcoinPaymentUrl() /" +
                                " Info : {}.", LocalDateTime.now(), "Bitcoin API returned payment URL. Bitcoin payment process started. Transaction status : PENDING.");
                    }
                    else {
                        urlDTO.setUrl(buyerRequestService.getErrorUrl(buyerRequestId));
                        logger.error("Date : {}, Bitcoin API / " + "BuyerRequestId = " + buyerRequestId + " / Method : getBitcoinPaymentUrl() /" +
                                " Error : {}.", LocalDateTime.now(), "Bitcoin API didn't return payment URL. Bitcoin payment process failed. Transaction status : FAILED.");
                    }
                }
                else {
                    urlDTO.setUrl(buyerRequestService.getErrorUrl(buyerRequestId));
                    logger.error("Date : {}, Bitcoin API / " + "BuyerRequestId = " + buyerRequestId + " / Method : getBitcoinPaymentUrl() /" +
                            " Error : {}.", LocalDateTime.now(), "Bitcoin API didn't return response. Bitcoin payment process failed. Transaction status : FAILED.");
                }
            }
            else {
                logger.error("Date : {}, Bitcoin API / " + "BuyerRequestId = " + buyerRequestId + " / Method : getBitcoinPaymentUrl() /" +
                        " Error : {}.", LocalDateTime.now(), "Invalid BuyerRequestId. Transaction status : FAILED.");
                urlDTO.setUrl(buyerRequestService.getFailedUrl(buyerRequestId));
            }
        }
        catch (Exception e){
            urlDTO.setUrl(buyerRequestService.getErrorUrl(buyerRequestId));
            logger.error("Date : {}, Bitcoin API / " + "BuyerRequestId = " + buyerRequestId + " / Method : getBitcoinPaymentUrl() /" +
                    " Error : {}.", LocalDateTime.now(), e.getMessage(), "/ Transaction status : ERROR.");
            e.printStackTrace();
        }
        return new ResponseEntity<>(urlDTO, HttpStatus.OK);
    }

    private OrderResult getBitcoinPaymentUrl(OrderRequest orderRequest){
        OrderResult orderResult = null;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<OrderRequest> body = new HttpEntity<>(orderRequest, headers);

            orderResult = restTemplate.exchange("https://bitcoin/order",
                    HttpMethod.POST, body, new ParameterizedTypeReference<OrderResult>() {}).getBody();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return orderResult;
    }
}
