package com.la.service.impl;

import com.la.model.dtos.*;
import com.la.model.Payment;
import com.la.model.*;
import com.la.model.enums.Status;
import com.la.repository.*;
import com.la.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.management.OperationsException;
import javax.validation.ValidationException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    MerchantRepository merchantRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RestTemplate restTemplate;

    public BankPaymentUrlDTO createPayment(BankRequestDTO bankRequestDTO) {
        // Check if merchant id exists
        // If exists create Payment object in database
        // Create nedeed DTO and return

        // COMPARE PASSWORDS???

        if(merchantRepository.findById(bankRequestDTO.getMerchantId()).isPresent()){
            Payment payment = new Payment();
            payment.setMerchantTimestamp(bankRequestDTO.getMerchantTimestamp());
            payment.setAmount(bankRequestDTO.getAmount());
            payment.setMerchantOrderId(bankRequestDTO.getMerchantOrderId());
            payment.setMerchant(merchantRepository.getOne(bankRequestDTO.getMerchantId()));
            payment = paymentRepository.save(payment);

            return new BankPaymentUrlDTO(payment.getId(), "https://localhost:3002/" + payment.getId());
        }

        return null;
    }

    public BankResponseDTO createTransaction(TransactionFormDataDTO transactionFormDataDTO, Long paymentId) {
        Payment payment = new Payment();
        Account account = new Account();
        Transaction transaction = new Transaction();
        try {
            payment = this.paymentRepository.findById(paymentId).get();
            System.err.println(payment);
            if (payment.equals(null)) {
                throw new NullPointerException();
            }
            if (!transactionFormDataDTO.getPan().matches("^[0-9]*$") || transactionFormDataDTO.getPan().length() < 14 || transactionFormDataDTO.getPan().length() > 16 ) {
                throw new ValidationException();
            }
            if (!transactionFormDataDTO.getSecurityCode().matches("^[0-9]*$") || transactionFormDataDTO.getSecurityCode().length() < 3 || transactionFormDataDTO.getSecurityCode().length() > 4) {
                throw new ValidationException();
            }
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-512");
                byte[] bytes = md.digest(transactionFormDataDTO.getPan().getBytes(StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                for(int i=0; i< bytes.length ;i++){
                    sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
                }
                String pan = sb.toString();
                MessageDigest md1 = MessageDigest.getInstance("SHA-512");
                byte[] bytes1 = md1.digest(transactionFormDataDTO.getSecurityCode().getBytes(StandardCharsets.UTF_8));
                StringBuilder sb1 = new StringBuilder();
                for(int i=0; i< bytes1.length ;i++){
                    sb1.append(Integer.toString((bytes1[i] & 0xff) + 0x100, 16).substring(1));
                }
                String securityCode = sb1.toString();
                account = (cardRepository.findByPanAndSecurityCodeAndExpireDateAndCardholderName(pan, securityCode, transactionFormDataDTO.getExpireDate(), transactionFormDataDTO.getCardholderName())).getAccount();
                if (account.getBalance() < payment.getAmount()) {
                    throw new OperationsException();
                }
                account.setBalance(account.getBalance() - payment.getAmount());
                this.accountRepository.save(account);

            } catch (NullPointerException e) {
                PCCRequestDTO pccRequestDTO = new PCCRequestDTO(transactionFormDataDTO.getPan(),transactionFormDataDTO.getSecurityCode(),transactionFormDataDTO.getExpireDate(),transactionFormDataDTO.getCardholderName(),payment.getMerchantOrderId(),payment.getMerchantTimestamp(),payment.getAmount());
                PCCResponseDTO pccResponseDTO = this.findIssuerBank(pccRequestDTO);
                account = null;
                if (pccResponseDTO.getStatus().equals(Status.ERROR)) {
                    transaction.setTimestamp(LocalDateTime.now());
                    transaction.setStatus(Status.ERROR);
                    transaction.setPayment(payment);
                    transaction.setIssuerTimestamp(null);
                    transaction.setIssuerOrderId(null);
                    transaction = transactionRepository.save(transaction);
                    return new BankResponseDTO(payment.getMerchantOrderId(), transaction.getId(),
                            transaction.getTimestamp(),paymentId, Status.ERROR);
                } else if (pccResponseDTO.getStatus().equals(Status.FAILED)) {
                    transaction.setTimestamp(LocalDateTime.now());
                    transaction.setStatus(Status.FAILED);
                    transaction.setPayment(payment);
                    transaction.setIssuerTimestamp(pccResponseDTO.getIssuerTimestamp());
                    transaction.setIssuerOrderId(pccResponseDTO.getIssuerOrderId());
                    transaction = transactionRepository.save(transaction);
                    return new BankResponseDTO(payment.getMerchantOrderId(), transaction.getId(),
                            transaction.getTimestamp(),paymentId, Status.FAILED);
                }
                System.out.println(pccResponseDTO.toString());
            }

            Account merchantAccount = payment.getMerchant().getAccount();
            merchantAccount.setBalance(merchantAccount.getBalance() + payment.getAmount());
            this.accountRepository.save(merchantAccount);

            transaction.setAccount(account);
            transaction.setPayment(payment);
            transaction.setStatus(Status.SUCCESS);
            transaction.setIssuerOrderId(null);
            transaction.setIssuerTimestamp(null);
            transaction.setTimestamp(LocalDateTime.now());
            transaction = transactionRepository.save(transaction);
        } catch (OperationsException e) {
            transaction.setAccount(account);
            transaction.setPayment(payment);
            transaction.setStatus(Status.FAILED);
            transaction.setIssuerOrderId(null);
            transaction.setIssuerTimestamp(null);
            transaction.setTimestamp(LocalDateTime.now());
            transaction = transactionRepository.save(transaction);
            return new BankResponseDTO(payment.getMerchantOrderId(), transaction.getId(),
                    transaction.getTimestamp(),paymentId, Status.FAILED);
        } catch (ValidationException | IndexOutOfBoundsException e) {
            transaction.setTimestamp(LocalDateTime.now());
            transaction.setStatus(Status.ERROR);
            transaction.setPayment(payment);
            transaction.setIssuerTimestamp(null);
            transaction.setIssuerOrderId(null);
            transaction = transactionRepository.save(transaction);
            return new BankResponseDTO(payment.getMerchantOrderId(), transaction.getId(),
                    transaction.getTimestamp(),paymentId, Status.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            transaction.setTimestamp(LocalDateTime.now());
            transaction.setStatus(Status.ERROR);
            transaction = transactionRepository.save(transaction);
            return new BankResponseDTO(null, transaction.getId(),
                    transaction.getTimestamp(),paymentId, Status.ERROR);
        }
        return new BankResponseDTO(payment.getMerchantOrderId(), transaction.getId(),
                transaction.getTimestamp(),paymentId, Status.SUCCESS);
    }

    @Override
    public Payment getPayment(Long paymentId) {
        return paymentRepository.findById(paymentId).get();
    }

    @Override
    public PCCResponseDTO createSecondTransaction(PCCRequestDTO pccRequestDTO) {
        Account account = new Account();
        PCCResponseDTO pccResponseDTO = new PCCResponseDTO();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(pccRequestDTO.getPan().getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            String pan = sb.toString();
            MessageDigest md1 = MessageDigest.getInstance("SHA-512");
            byte[] bytes1 = md1.digest(pccRequestDTO.getSecurityCode().getBytes(StandardCharsets.UTF_8));
            StringBuilder sb1 = new StringBuilder();
            for(int i=0; i< bytes1.length ;i++){
                sb1.append(Integer.toString((bytes1[i] & 0xff) + 0x100, 16).substring(1));
            }
            String securityCode = sb1.toString();
            account = (cardRepository.findByPanAndSecurityCodeAndExpireDateAndCardholderName(pan, securityCode, pccRequestDTO.getExpireDate(), pccRequestDTO.getCardholderName())).getAccount();
        } catch (NullPointerException | NoSuchAlgorithmException e) {
            pccResponseDTO.setStatus(Status.ERROR);
            return pccResponseDTO;
        }
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setIssuerOrderId(null);
        transaction.setIssuerTimestamp(null);
        transaction.setPayment(null); //amount
        transaction = transactionRepository.save(transaction);

        pccResponseDTO.setIssuerTimestamp(LocalDateTime.now());
        pccResponseDTO.setIssuerOrderId(transaction.getId());
        pccResponseDTO.setAcquirerOrderId(pccRequestDTO.getAcquirerOrderId());
        pccResponseDTO.setAcquirerTimestamp(pccRequestDTO.getAcquirerTimestamp());

        if (account.getBalance() < pccRequestDTO.getAmount()) {
            transaction.setStatus(Status.FAILED);
            transaction = transactionRepository.save(transaction);
            pccResponseDTO.setStatus(Status.FAILED);
        } else {
            transaction.setStatus(Status.SUCCESS);
            transaction = transactionRepository.save(transaction);
            pccResponseDTO.setStatus(Status.SUCCESS);
            account.setBalance(account.getBalance() - pccRequestDTO.getAmount());
            accountRepository.save(account);
        }
        return pccResponseDTO;
    }


    public PCCResponseDTO findIssuerBank(PCCRequestDTO pccRequestDTO) {
        return restTemplate.exchange("https://pcc/bank",
                HttpMethod.POST, new HttpEntity<>(pccRequestDTO), new ParameterizedTypeReference<PCCResponseDTO>() {}).getBody();
    }

}
