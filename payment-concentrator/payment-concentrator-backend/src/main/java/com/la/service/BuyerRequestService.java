package com.la.service;

import com.la.model.dtos.PaymentMethodsBuyerRequestDTO;

public interface BuyerRequestService {

    PaymentMethodsBuyerRequestDTO get(Long id);

    String getErrorUrl(Long id);

    String getFailedUrl(Long id);
}
