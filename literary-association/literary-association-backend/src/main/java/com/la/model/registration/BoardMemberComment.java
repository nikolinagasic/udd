package com.la.model.registration;

import com.la.model.enums.Opinion;
import com.la.model.users.BoardMember;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class BoardMemberComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String text;

    @Column
    private Date date;

    @Column
    @Enumerated(EnumType.STRING)
    private Opinion opinion;

    @ManyToOne
    @JoinColumn(name = "writer_membership_request_id")
    private WriterMembershipRequest writerMembershipRequest;

    @ManyToOne
    @JoinColumn(name = "board_member_id")
    private BoardMember boardMember;

    @Column
    private Boolean reviewed;
}
