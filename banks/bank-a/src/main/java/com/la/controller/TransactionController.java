package com.la.controller;

import com.la.model.Payment;
import com.la.model.enums.Status;
import com.la.model.dtos.*;
import com.la.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;

@RestController
@CrossOrigin(value = "https://localhost:3002")
@RequestMapping(value = "/transaction")
public class TransactionController {

    Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * POST transaction/
     *
     * Payment Concetrator calls this endpoint to get bank payment url and id
     *
     * @return bank payment url and id object
     */
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BankPaymentUrlDTO> createPayment(@RequestBody BankRequestDTO bankRequestDTO) {
        try {
            BankPaymentUrlDTO bankPaymentUrlDTO = transactionService.createPayment(bankRequestDTO);
            return new ResponseEntity<>(bankPaymentUrlDTO, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * GET transaction/
     *
     *
     * @return bank payment object
     */
    @GetMapping(value = "/payment/{paymentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Payment> getPayment(@PathVariable Long paymentId) {
        try {
            Payment payment = transactionService.getPayment(paymentId);
            return new ResponseEntity<>(payment, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * POST transaction/
     *
     * Buyer submits form
     *
     * @return STATUS URL WHICH PAYMENT CONCETRATOR RETURNS
     */
    @PostMapping(value = "/{paymentId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UrlDTO> createTransaction(@RequestBody TransactionFormDataDTO transactionFormDataDTO, @PathVariable Long paymentId) {
        try {
            BankResponseDTO bankResponseDTO = this.transactionService.createTransaction(transactionFormDataDTO, paymentId);

            System.err.println(bankResponseDTO);
            UrlDTO urlDTO = getStatusUrl(bankResponseDTO);

            if (bankResponseDTO.getStatus().equals(Status.ERROR)) {
                logger.warn("Date : {}, Wrong user input while paying with bank card. " +
                        "Warn : {}.", LocalDateTime.now(), "Bank transaction error");
            } else if (bankResponseDTO.getStatus().equals(Status.FAILED)) {
                logger.warn("Date : {}, Not enough money in user's bank account. " +
                        "Warn : {}.", LocalDateTime.now(), "Bank transaction failed");
            } else {
                logger.info("Date : {}, Bank payment successful. " +
                        "Info : {}.", LocalDateTime.now(), "Successful bank transaction");
            }
            return new ResponseEntity<>(urlDTO, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Date : {}, Error while paying with bank card. " +
                    "Error : {}.", LocalDateTime.now(), e.toString());

            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Calls payment concetrator to update transaction status
     *
     * @return STATUS URL (SUCCESS, FAILED, ERROR)
     */
    private UrlDTO getStatusUrl(BankResponseDTO bankResponseDTO) {
        return restTemplate.exchange("https://zuul-api-gateway/api/auth/bank/transaction/update",
                HttpMethod.PUT, new HttpEntity<>(bankResponseDTO), new ParameterizedTypeReference<UrlDTO>() {}).getBody();
    }

    @PostMapping(value = "/second", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PCCResponseDTO createSecondTransaction(@RequestBody PCCRequestDTO pccRequestDTO) {
        return this.transactionService.createSecondTransaction(pccRequestDTO);
    }
}
