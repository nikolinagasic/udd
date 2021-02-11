package com.la.model.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreatedBookPurchaseRequestDTO {
    private Long orderId;
    private String timestamp;
    private Double amount;
    private String token;
    private String username;
}
