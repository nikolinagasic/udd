package com.la.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class SubscriberDetails {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column
    private String organizationName;

    @Column
    private String organizationDescription;

    @Column
    private String organizationEmail;

    @Column(name = "merchant_id")
    private String merchantId;

    @Column(name = "merchant_password")
    private String merchantPassword;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_secret")
    private String clientSecret;

    @Column(name = "bitcoin_token")
    private String bitcoinToken;

    @Column(name = "merchant_success_url", nullable = false)
    private String successUrl;

    @Column(name = "merchant_failed_url", nullable = false)
    private String failedUrl;

    @Column(name = "merchant_error_url", nullable = false)
    private String errorUrl;
}
