package com.la.model.dtos.bank;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BankPaymentUrlDTO {
    private Long paymentId;
    private String paymentUrl;
}
