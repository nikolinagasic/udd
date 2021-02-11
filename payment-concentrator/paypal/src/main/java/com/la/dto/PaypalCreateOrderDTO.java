package com.la.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaypalCreateOrderDTO {
    private String orderId;
    private String redirectUrl;
    private Long buyerRequestId;
}
