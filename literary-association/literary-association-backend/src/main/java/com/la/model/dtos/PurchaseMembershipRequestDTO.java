package com.la.model.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PurchaseMembershipRequestDTO {
    private Long orderId;
    private Double amount;
    private String token;
    private String username;
}
