package com.la.model.dtos.paypal;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaypalCreateOrderDTO {
    private String orderId;
    private String redirectUrl;
    private Long buyerRequestId;
}
