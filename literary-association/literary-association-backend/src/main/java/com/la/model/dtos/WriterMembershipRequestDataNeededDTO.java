package com.la.model.dtos;

import com.la.model.enums.WriterMembershipStatus;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WriterMembershipRequestDataNeededDTO {
    private WriterMembershipStatus status;
    private Date paymentDeadline;
    private Integer attemptsNumber;
    private Date submissionDeadline;
    private Integer filesPosted;
}
