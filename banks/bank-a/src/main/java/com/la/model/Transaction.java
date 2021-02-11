package com.la.model;

import com.la.model.enums.Status;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // acqOrderId

    @Column
    private LocalDateTime timestamp; // acqTimestamp

    @Column
    private Long issuerOrderId; // Buyer bank transaction id (if different banks)

    @Column
    private LocalDateTime issuerTimestamp; // Buyer bank transaction timestamp (if different banks)

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment; // Delegated from LA to Payment Concetrator to Bank

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;
}
