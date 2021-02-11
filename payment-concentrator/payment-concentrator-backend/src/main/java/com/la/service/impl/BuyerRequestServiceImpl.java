package com.la.service.impl;

import com.la.model.dtos.BuyerRequestDTO;
import com.la.model.dtos.PaymentMethodsBuyerRequestDTO;
import com.la.model.BuyerRequest;
import com.la.repository.BuyerRequestRepository;
import com.la.service.BuyerRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuyerRequestServiceImpl implements BuyerRequestService {

    @Autowired
    private BuyerRequestRepository buyerRequestRepository;

    @Override
    public PaymentMethodsBuyerRequestDTO get(Long id){

        if(buyerRequestRepository.findById(id).isPresent()){
            BuyerRequest buyerRequest =  buyerRequestRepository.findById(id).get();

            PaymentMethodsBuyerRequestDTO paymentMethodsBuyerRequestDTO = new PaymentMethodsBuyerRequestDTO();

            paymentMethodsBuyerRequestDTO.setPaymentMethods(buyerRequest.getSubscriber().getPaymentMethods());
            paymentMethodsBuyerRequestDTO.setUsername(buyerRequest.getSubscriber().getUsername());

            BuyerRequestDTO buyerRequestDTO = new BuyerRequestDTO();
            buyerRequestDTO.setAmount(buyerRequest.getAmount());
            buyerRequestDTO.setMerchantOrderId(buyerRequest.getId());
            buyerRequestDTO.setMerchantTimestamp(buyerRequest.getMerchantTimestamp());

            paymentMethodsBuyerRequestDTO.setBuyerRequestDTO(buyerRequestDTO);

            return paymentMethodsBuyerRequestDTO;
        }

        return null;
    }

    @Override
    public String getErrorUrl(Long id) {
        return buyerRequestRepository.findById(id).get().getSubscriber().getSubscriberDetails().getErrorUrl();
    }

    @Override
    public String getFailedUrl(Long id) {
        return buyerRequestRepository.findById(id).get().getSubscriber().getSubscriberDetails().getFailedUrl();
    }


}
