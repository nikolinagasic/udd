package com.la.model.dtos;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubscriptionRequestDTO {

    private Long id;

    @NotEmpty
    private String organizationName;

    @NotEmpty
    private String organizationDescription;

    @NotEmpty
    private String organizationEmail;

    @NotEmpty
    private Set<PaymentMethodDTO> paymentMethods;

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    private String merchantId;
    private String merchantPassword;

    private String clientId;
    private String clientSecret;

    private String bitcoinToken;

    @NotEmpty
    private String successUrl;

    @NotEmpty
    private String errorUrl;

    @NotEmpty
    private String failedUrl;
}
