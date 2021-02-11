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
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String pan;

    @Column
    private String securityCode;

    @Column
    private String expireDate;

    @Column
    private String cardholderName;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
