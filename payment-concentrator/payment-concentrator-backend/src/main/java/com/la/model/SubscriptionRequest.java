package com.la.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class SubscriptionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column
    private String organizationName;

    @Column
    @NotNull
    private String organizationDescription;

    @Column
    @NotNull
    private String organizationEmail;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String merchantId;

    @Column
    private String merchantPassword;

    @Column
    private String clientId;

    @Column
    private String clientSecret;

    @Column
    private String bitcoinToken;

    @Column
    private String successUrl;

    @Column
    private String errorUrl;

    @Column
    private String failedUrl;

    @ManyToMany()
    @JoinTable(name = "subscription_requests_payment_methods",
            joinColumns = @JoinColumn(name = "request_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "payment_method_id", referencedColumnName = "id"))
    private Set<PaymentMethod> paymentMethods;
}
