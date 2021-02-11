package com.la.security.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import com.la.model.Transaction;
import com.la.model.dtos.paypal.PaypalCreateOrderDTO;
import com.la.model.dtos.paypal.PaypalOrderDTO;
import com.la.model.PaymentMethod;
import com.la.model.Subscriber;
import com.la.model.enums.Status;
import com.la.repository.PaymentMethodRepository;
import com.la.repository.SubscriberRepository;
import com.la.repository.TransactionRepository;
import com.la.security.UserTokenState;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@CrossOrigin(value = "https://localhost:3000")
public class PaymentFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    Logger logger = LoggerFactory.getLogger(PaymentMethod.class);

    private void setFailedRequest(String body, int code) {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.setResponseStatusCode(code);
        if (ctx.getResponseBody() == null) {
            ctx.setResponseBody(body);
            ctx.setSendZuulResponse(false);
        }
    }

    @Override
    public Object run() throws ZuulException {
        System.out.println("Usao je u zuul");

        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();


        PaypalOrderDTO paypalOrderDTO = null;
        try {
            paypalOrderDTO = extractBody(context);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if (request.getRequestURI().contains("payPal/create")) {
            if (paypalOrderDTO == null || paypalOrderDTO.getMerchantOrderId() == null || paypalOrderDTO.getMerchantTimestamp() == null
                    || paypalOrderDTO.getAmount() == null || paypalOrderDTO.getUserId() == null) {
                logger.error("Date : {}, A user with id {} tried to create order. Not enough data for request.", LocalDateTime.now(), paypalOrderDTO.getUserId());
                setFailedRequest("Request does not contain all data.", 400);
                return null;
            }
            logger.info("Date : {}, A user with id {} tried to create order.", LocalDateTime.now(), paypalOrderDTO.getUserId());
            context.set("merchantOrderId", paypalOrderDTO.getMerchantOrderId());
            context.set("merchantTimestamp", paypalOrderDTO.getMerchantTimestamp());
            context.set("buyerRequestId", paypalOrderDTO.getBuyerRequestId());
        }

        if (request.getRequestURI().contains("pay-pal/capture")) {
            logger.info("Date : {}, A user with id {} tried to capture order.", LocalDateTime.now(), paypalOrderDTO.getUserId());
            context.set("orderId", request.getRequestURI().split("/")[3]);
        }

        if (request.getRequestURI().contains("pay-pal/subscribe")) {
            context.set("merchantOrderId", paypalOrderDTO.getMerchantOrderId());
        }


        return null;
    }


    private PaypalOrderDTO extractBody(RequestContext context) throws JsonProcessingException {
        InputStream in = (InputStream) context.get("requestEntity");
        if (in == null) {
            try {
                in = context.getRequest().getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        String body = null;
        try {
            body = StreamUtils.copyToString(in, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        System.out.println("telo je " + body);
        PaypalOrderDTO paypalOrderDTO = null;
        try {
            paypalOrderDTO = new ObjectMapper().readValue(body, PaypalOrderDTO.class);
            System.out.println("kad parsira telo");
            System.out.println(paypalOrderDTO);
        } catch (JsonMappingException e) {
//            e.printStackTrace();
            return null;
        }
        return paypalOrderDTO;
    }
}
