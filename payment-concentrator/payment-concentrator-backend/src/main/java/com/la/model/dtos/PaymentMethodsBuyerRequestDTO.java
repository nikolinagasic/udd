package com.la.model.dtos;

import com.la.model.PaymentMethod;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentMethodsBuyerRequestDTO {

    private String username;
    private BuyerRequestDTO buyerRequestDTO;
    private Set<PaymentMethod> paymentMethods;
}
