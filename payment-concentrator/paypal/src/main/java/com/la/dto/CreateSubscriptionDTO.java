package com.la.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateSubscriptionDTO {
    private Long productId;
    private Long merchantOrderId;
    private Double amount;
    private String username;
}
