package com.la.controller;

import com.la.model.dtos.SubscriberUpdateTransactionDTO;
import com.la.model.dtos.UrlDTO;
import com.la.model.dtos.bank.BankPaymentUrlDTO;
import com.la.model.dtos.bank.BankRequestDTO;
import com.la.model.dtos.bank.BankResponseDTO;
import com.la.service.BankTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/auth/bank/transaction")
public class BankTransactionController {

    @Autowired
    private BankTransactionService transactionService;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * POST transaction/
     *
     * Buyer from LA initiates making of new transaction
     *
     * @return bank payment form URL
     */
    @PostMapping(value = "/{buyerRequestId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UrlDTO> create(@PathVariable Long buyerRequestId) {
        try {
            BankRequestDTO bankRequestDTO = transactionService.createBankRequestDTO(buyerRequestId);
            System.err.println(bankRequestDTO);
            if (bankRequestDTO != null){
                    // If does update transaction payment ID in database
                    // If bank doesnt return form call LA to update order status to FAILED
                    BankPaymentUrlDTO bankPaymentUrlDTO = getPaymentFormUrl(bankRequestDTO);
                    if (bankPaymentUrlDTO != null) {
                        transactionService.updateTransactionPaymentId(bankPaymentUrlDTO.getPaymentId(), buyerRequestId);
                        return new ResponseEntity<>(new UrlDTO(bankPaymentUrlDTO.getPaymentUrl()), HttpStatus.OK);
                    }
                    String errorUrl = transactionService.updateTransactionError(buyerRequestId);
//                  finishTransaction(new SubscriberUpdateTransactionDTO(bankRequestDTO.getMerchantOrderId(), Status.FAILED));

                    return new ResponseEntity<>(new UrlDTO(errorUrl), HttpStatus.PRECONDITION_FAILED);
            }
            else {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * PUT transaction
     *
     * Bank returns payment status
     *
     * @return STATUS URL (SUCCESS, FAILED, ERROR)
     */
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UrlDTO> update(@RequestBody BankResponseDTO bankResponseDTO) {
        try {
            String statusUrl = transactionService.updateTransaction(bankResponseDTO);
            System.err.println(statusUrl);
//            finishTransaction(new SubscriberUpdateTransactionDTO(bankResponseDTO.getMerchantOrderId(), bankResponseDTO.getStatus()));
            if (statusUrl != null){
                return new ResponseEntity<>(new UrlDTO(statusUrl), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Calls bank service
     *
     * @return bank payment form URL and ID
     */
    private BankPaymentUrlDTO getPaymentFormUrl(BankRequestDTO bankRequestDTO) {
        return restTemplate.exchange("https://bank-a/transaction",
                HttpMethod.POST, new HttpEntity<>(bankRequestDTO), new ParameterizedTypeReference<BankPaymentUrlDTO>() {}).getBody();
    }

    /**
     * Calls literary assocciation service to update transaction status
     *
     * @return
     */
    private String finishTransaction(SubscriberUpdateTransactionDTO subscriberUpdateTransactionDTO) {
//        String response = restTemplate.exchange("http://localhost:8080/order/{merchantOrderId}",
//                HttpMethod.PUT, new HttpEntity<>(), new ParameterizedTypeReference<String>() {}).getBody();
        // NEED FEIGN CLIENT

        return "";
    }
}
