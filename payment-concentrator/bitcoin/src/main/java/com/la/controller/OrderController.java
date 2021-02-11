package com.la.controller;

import com.la.model.OrderRequest;
import com.la.model.OrderResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    RestTemplate restTemplate;
    
    @Value("${bitcoin.token}")
    String bitcoinToken;

    @PostMapping(value = "")
    public ResponseEntity<OrderResult> create(@RequestBody OrderRequest orderRequest) {

        OrderResult orderResult = null;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(bitcoinToken);

            HttpEntity<OrderRequest> body = new HttpEntity<>(orderRequest, headers);

            orderResult = restTemplate.exchange("https://api-sandbox.coingate.com/v2/orders",
                    HttpMethod.POST, body, new ParameterizedTypeReference<OrderResult>() {}).getBody();

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(orderResult, HttpStatus.OK);
    }

    @GetMapping(value = "/{orderId}")
    public ResponseEntity<OrderResult> get(@PathVariable Long orderId) {

        OrderResult orderResult = null;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(bitcoinToken);

            HttpEntity<String> body = new HttpEntity<>("parameters", headers);

            orderResult = restTemplate.exchange("https://api-sandbox.coingate.com/v2/orders/" + orderId,
                    HttpMethod.GET, body, new ParameterizedTypeReference<OrderResult>() {}).getBody();

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(orderResult, HttpStatus.OK);
    }

}
