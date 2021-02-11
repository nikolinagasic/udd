package com.la.model.registration;

import com.la.model.enums.WriterMembershipStatus;
import com.la.model.users.SysUser;
import com.la.model.users.User;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@DiscriminatorValue("WRITER_MEMBERSHIP_REQUEST")
public class WriterMembershipRequest extends SysUser implements Serializable {

    @Column
    @Enumerated(EnumType.STRING)
    private WriterMembershipStatus status;

    @Column
    private boolean activated;

    @Column
    private Date paymentDeadline;

    @Column
    private Integer attemptsNumber;

    @Column
    private Date submissionDeadline;

    @Column
    private Integer filesPosted;
}
