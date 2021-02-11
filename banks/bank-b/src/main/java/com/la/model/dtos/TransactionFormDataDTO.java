package com.la.model.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransactionFormDataDTO {

    private String pan;

    private String securityCode;

    private String cardholderName;

    private String expireDate;
}
