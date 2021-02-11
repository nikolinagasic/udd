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
public class BuyerRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "subscriber_id", referencedColumnName = "id")
    Subscriber subscriber;

    @Column
    private Long merchantOrderId; // Order Id from LA

    @Column
    private LocalDateTime merchantTimestamp;

    @Column
    private LocalDateTime timestamp;

    @Column
    private float amount;

    @Column
    private String description;

}
