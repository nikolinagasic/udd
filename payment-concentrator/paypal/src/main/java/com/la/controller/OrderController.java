package com.la.controller;

import com.la.Credentials;
import com.la.dto.CreateOrderDTO;
import com.la.dto.PaypalCreateOrderDTO;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/")
public class OrderController {

    @PostMapping(value = "/create")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderDTO orderDTO) {
        System.err.println("hehic");
        Order order = null;
        // Construct a request object and set desired parameters
        // Here, OrdersCreateRequest() creates a POST request to /v2/checkout/orders
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");
        orderRequest.applicationContext(new ApplicationContext().returnUrl("https://localhost:3000/success/" + orderDTO.getMerchantOrderId()));
        List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();
        purchaseUnits
                .add(new PurchaseUnitRequest().amountWithBreakdown
                        (new AmountWithBreakdown().currencyCode("USD").value(orderDTO.getAmount().toString())));
        orderRequest.purchaseUnits(purchaseUnits);
        OrdersCreateRequest request = new OrdersCreateRequest().requestBody(orderRequest);

        try {
            // Call API with your client and get a response for your call
            HttpResponse<Order> response = Credentials.client.execute(request);

            // If call returns body in response, you can get the de-serialized version by
            // calling result() on the response
            order = response.result();
            System.out.println("Order ID: " + order.id());
            order.links().forEach(link -> System.out.println(link.rel() + " => " + link.method() + ":" + link.href()));

            return new ResponseEntity<>(new PaypalCreateOrderDTO(order.id(), order.links().get(1).href(), orderDTO.getBuyerRequestId()), HttpStatus.CREATED);
        } catch (Exception ioe) {
            ioe.printStackTrace();
            return new ResponseEntity<>("Order has not been created. You must first approve payment.", HttpStatus.BAD_REQUEST);
        }
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/capture/{id}")
    public ResponseEntity<?> capture(@PathVariable("id") String orderId) {
        Order order = null;
        OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);

        try {
            // Call API with your client and get a response for your call
            HttpResponse<Order> response = Credentials.client.execute(request);

            // If call returns body in response, you can get the de-serialized version by
            // calling result() on the response
            order = response.result();
            System.out.println("Capture ID: " + order.purchaseUnits().get(0).payments().captures().get(0).id());
            order.purchaseUnits().get(0).payments().captures().get(0).links()
                    .forEach(link -> System.out.println(link.rel() + " => " + link.method() + ":" + link.href()));

            return new ResponseEntity<>(new PaypalCreateOrderDTO(orderId, order.links().get(0).href(), null), HttpStatus.CREATED);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return new ResponseEntity<>("Order has not been captured.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getOrder(@PathVariable("id") String orderId) {
        Order order = null;
        OrdersGetRequest request = new OrdersGetRequest(orderId);
        try {
            HttpResponse<Order> response = Credentials.client.execute(request);

            // If call returns body in response, you can get the de-serialized version by
            // calling result() on the response
            order = response.result();
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
