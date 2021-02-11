package com.pcc.model.dtos;

import com.pcc.model.enums.Status;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PCCResponseDTO {

    private Long acquirerOrderId;

    private LocalDateTime acquirerTimestamp;

    private Long issuerOrderId;

    private LocalDateTime issuerTimestamp;

    private Status status;
}
