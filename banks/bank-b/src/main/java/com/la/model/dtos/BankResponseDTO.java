package com.la.model.dtos;

import com.la.model.enums.Status;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BankResponseDTO {

    private Long merchantOrderId;

    private Long acqOrderId;

    private LocalDateTime acqTimestamp;

    private Long paymentId;

    private Status status;
}
