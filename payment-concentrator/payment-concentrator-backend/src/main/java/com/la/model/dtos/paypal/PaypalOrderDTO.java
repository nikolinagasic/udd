package com.la.model.dtos.paypal;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaypalOrderDTO {
    private Long productId;
    private Long userId;
    private Long merchantOrderId;
    private String merchantTimestamp;
    private Double amount;
    private Long buyerRequestId;
    private String username;
}
