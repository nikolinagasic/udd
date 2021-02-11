package com.la.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import com.la.model.BuyerRequest;
import com.la.model.Subscriber;
import com.la.model.dtos.paypal.PaypalCreateOrderDTO;
import com.la.model.dtos.paypal.SubscriptionCreatedDTO;
import com.la.model.enums.Status;
import com.la.model.Transaction;
import com.la.repository.BuyerRequestRepository;
import com.la.repository.PaymentMethodRepository;
import com.la.repository.SubscriberRepository;
import com.la.repository.TransactionRepository;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
@CrossOrigin(value = "https://localhost:3000")
public class PostPaymentFilter extends ZuulFilter {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private BuyerRequestRepository buyerRequestRepository;

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    Logger logger = LoggerFactory.getLogger(PostPaymentFilter.class);

    @Override
    public Object run() throws ZuulException {
        System.out.println("usao je u post filter, valjda posle pazpala");

        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();

        if (request.getRequestURI().contains("pay-pal/create")) {
            logger.info("Date : {}, Order created, system tried to save in db.", LocalDateTime.now());
            saveTransaction(context);
        }

        if (request.getRequestURI().contains("pay-pal/capture")) {
            Transaction transaction = transactionRepository.findByAcqOrderId(context.get("orderId").toString());
            transaction.setStatus(Status.SUCCESS);
            transactionRepository.save(transaction);
            logger.info("Date : {} System successfully updated order with id {} in db.", LocalDateTime.now(), transaction.getAcqOrderId());
        }

        if (request.getRequestURI().contains("pay-pal/subscribe")) {
            try (final InputStream responseDataStream = context.getResponseDataStream()) {

                String responseData = CharStreams.toString(new InputStreamReader(responseDataStream, "UTF-8"));

                context.setResponseBody(responseData);
                System.out.println(responseData);
                SubscriptionCreatedDTO dto = new ObjectMapper().readValue(responseData, SubscriptionCreatedDTO.class);


                BuyerRequest buyerRequest = new BuyerRequest();
                buyerRequest.setAmount(Float.parseFloat(dto.getAmount().toString()));
                buyerRequest.setMerchantOrderId(dto.getOrderId());
                buyerRequest.setMerchantTimestamp(LocalDateTime.now());
                buyerRequest.setTimestamp(LocalDateTime.now());
                buyerRequest.setSubscriber(subscriberRepository.findByUsername("vulkan"));
                buyerRequest.setDescription(dto.getDescription());
                buyerRequestRepository.save(buyerRequest);

                Transaction transaction = new Transaction();
                transaction.setAcqOrderId(dto.getOrderId().toString());
                transaction.setAcqTimestamp(LocalDateTime.now());
                transaction.setTimestamp(LocalDateTime.now());
                transaction.setBuyerRequest(buyerRequest);
                transaction.setStatus(Status.SUCCESS);
                transaction.setPaymentMethod(paymentMethodRepository.findById(2L).get());
                transactionRepository.save(transaction);

                logger.info("Date : {} System successfully saved order with id {} in db.", LocalDateTime.now(), dto.getOrderId());
            } catch (Exception e) {
                logger.info("Date : {} System failed to save order in db.", LocalDateTime.now());

                e.printStackTrace();
            }


        }

        return null;
    }

    private void saveTransaction(RequestContext context) {
        System.out.println("merchantOrderId" + context.get("merchantOrderId"));
        System.out.println("merchantTimestamp" + context.get("merchantTimestamp"));

        try (final InputStream responseDataStream = context.getResponseDataStream()) {

            if (responseDataStream == null) {
                return;
            }

            String responseData = CharStreams.toString(new InputStreamReader(responseDataStream, "UTF-8"));

            context.setResponseBody(responseData);
            System.out.println(responseData);
            PaypalCreateOrderDTO orderDTO = new ObjectMapper().readValue(responseData, PaypalCreateOrderDTO.class);

            Transaction transaction = new Transaction();
            transaction.setAcqOrderId(orderDTO.getOrderId());
            transaction.setAcqTimestamp(LocalDateTime.now());
            transaction.setTimestamp(LocalDateTime.now());
            transaction.setBuyerRequest(buyerRequestRepository.findById(orderDTO.getBuyerRequestId()).get());
            transaction.setStatus(Status.PENDING);
            transaction.setPaymentMethod(paymentMethodRepository.findById(2L).get());
            transactionRepository.save(transaction);

            logger.info("Date : {} System successfully saved order with id {} in db.", LocalDateTime.now(), orderDTO.getOrderId());
        } catch (Exception e) {
            logger.info("Date : {} System failed to save order in db.", LocalDateTime.now());

            e.printStackTrace();
        }
    }

}
