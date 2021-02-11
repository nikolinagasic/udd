package com.la.service.impl;

import com.la.model.dtos.CreatedBookPurchaseRequestDTO;
import com.la.model.dtos.PurchaseBookRequestDTO;
import com.la.model.enums.TransactionStatus;
import com.la.model.mappers.PurchaseBookRequestDTOMapper;
import com.la.model.PurchaseBookRequest;
import com.la.repository.BookRepository;
import com.la.repository.PurchaseBookRequestRepository;
import com.la.repository.ReaderRepository;
import com.la.security.TokenUtils;
import com.la.security.UserTokenState;
import com.la.security.auth.JwtAuthenticationRequest;
import com.la.service.PurchaseBookRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.time.LocalDateTime;

@Service
public class PurchaseBookRequestServiceImpl implements PurchaseBookRequestService {

    @Autowired
    private PurchaseBookRequestRepository purchaseBookRequestRepository;

    @Autowired
    private PurchaseBookRequestDTOMapper purchaseBookRequestDTOMapper;

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${pc.username}")
    private String username;

    @Value("${pc.password}")
    private String password;

    @Override
    public CreatedBookPurchaseRequestDTO createPurchaseRequest(PurchaseBookRequestDTO purchaseBookRequestDTO) throws ParseException {
        // TO DO SET READER IF USER LOGGED IN
        //  Reader reader = (Reader) readerRepository.findByUsername(tokenUtils.getUsernameFromToken(token.substring(7)));

        /* ovo je ivanin kod, mislim da ce moj raditi i za tvoje
        PurchaseBookRequest request = new PurchaseBookRequest();
        request.setStatus(TransactionStatus.WAITING_PAYMENT);
        request.setAmount(purchaseBookRequestDTO.getAmount());
        System.err.println(purchaseBookRequestDTO.getBookList());
        List<Book> bookList = new ArrayList<>();
        for(Long id : purchaseBookRequestDTO.getBookList()){
            if(bookRepository.findById(id).isPresent()){
                bookList.add(bookRepository.findById(id).get());
            }
        }
        request.setBookList(bookList);
        System.err.println(bookList);
         */

        PurchaseBookRequest request = purchaseBookRequestDTOMapper.toEntity(purchaseBookRequestDTO);
        request.setStatus(TransactionStatus.WAITING_PAYMENT);
        request.setBookList(bookRepository.findAllById(purchaseBookRequestDTO.getBookList()));
//        request.setReader(reader);
        purchaseBookRequestRepository.saveAndFlush(request);

        JwtAuthenticationRequest jwtAuthenticationRequest = new JwtAuthenticationRequest(username, password);

        UserTokenState userTokenState = restTemplate.exchange("https://localhost:8081/api/auth/login",
                HttpMethod.POST, new HttpEntity<>(jwtAuthenticationRequest), new ParameterizedTypeReference<UserTokenState>() {}).getBody();

        return new CreatedBookPurchaseRequestDTO(request.getId(), LocalDateTime.now().toString(), request.getAmount(),
                userTokenState.getAccessToken(), tokenUtils.getUsernameFromToken(userTokenState.getAccessToken()));
    }

    @Override
    public boolean updateRequest(Long transactionId, String transactionStatus) {
        if (purchaseBookRequestRepository.findById(transactionId).isPresent()){
            PurchaseBookRequest purchaseBookRequest = purchaseBookRequestRepository.findById(transactionId).get();

            TransactionStatus status = TransactionStatus.FAILED;
            switch (transactionStatus) {
                case "PAYED" : {
                    status = TransactionStatus.PAYED;
                    break;
                }
                case "WAITING_PAYMENT" : {
                    status = TransactionStatus.WAITING_PAYMENT;
                    break;
                }
                case "FAILED" : {
                    status = TransactionStatus.FAILED;
                    break;
                }
            }

            purchaseBookRequest.setStatus(status);
            purchaseBookRequestRepository.saveAndFlush(purchaseBookRequest);

            return true;
        }
        return false;
    }
}
