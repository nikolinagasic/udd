package com.la.model.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BuyerRequestDTO {

    private Long merchantOrderId;
    private LocalDateTime merchantTimestamp;
    private float amount;
}
