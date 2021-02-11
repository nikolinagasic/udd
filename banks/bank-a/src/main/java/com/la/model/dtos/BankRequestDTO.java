package com.la.model.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BankRequestDTO{

    private Long merchantId;

    private String merchantPassword;

    private Long merchantOrderId; // Order Id from LA

    private LocalDateTime merchantTimestamp;

    private float amount;
}
