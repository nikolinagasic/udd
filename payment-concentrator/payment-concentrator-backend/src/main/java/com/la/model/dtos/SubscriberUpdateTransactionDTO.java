package com.la.model.dtos;

import com.la.model.enums.Status;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubscriberUpdateTransactionDTO {

    private Long merchantOrderId;
    private Status status;
}
