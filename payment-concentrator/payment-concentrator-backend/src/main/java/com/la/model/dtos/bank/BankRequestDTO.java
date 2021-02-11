package com.la.model.dtos.bank;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BankRequestDTO{

    private Long merchantId;
    private String merchantPassword;
    private Long merchantOrderId;
    private LocalDateTime merchantTimestamp;
    private float amount;
}
