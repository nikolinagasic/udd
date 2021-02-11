package com.la.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // payment_id ; payment_url example - /form/{paymentId}

    @OneToOne
    @JoinColumn(name="merchant_id", referencedColumnName = "id")
    private Merchant merchant;

    @Column
    private Long merchantOrderId;

    @Column
    private LocalDateTime merchantTimestamp;

    @Column
    private float amount;
}
