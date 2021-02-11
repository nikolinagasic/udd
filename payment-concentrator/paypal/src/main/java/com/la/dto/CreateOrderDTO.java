package com.la.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateOrderDTO {
    private Long userId;
    private Long merchantOrderId;
    private String merchantTimestamp;
    private Double amount;
    private Long buyerRequestId;
}
