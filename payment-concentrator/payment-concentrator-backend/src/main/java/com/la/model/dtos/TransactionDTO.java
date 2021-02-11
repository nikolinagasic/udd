package com.la.model.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransactionDTO {
    private String username;
    private String merchantOrderId;
    private String merchantTimestamp;
    private String acqOrderId;
    private String acqTimestamp;
    private String timestamp;
    private double amount;
    private String status;
    private String paymentMethod;
}
