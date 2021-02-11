package com.la.model.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PCCRequestDTO {

    private String pan;

    private String securityCode;

    private String expireDate;

    private String cardholderName;

    private Long acquirerOrderId;

    private LocalDateTime acquirerTimestamp;

    private float amount;



}
