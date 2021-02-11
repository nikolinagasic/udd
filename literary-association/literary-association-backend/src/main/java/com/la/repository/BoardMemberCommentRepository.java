package com.la.repository;

import com.la.model.registration.BoardMemberComment;
import com.la.model.registration.SubmittedWork;
import com.la.model.registration.WriterMembershipRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BoardMemberCommentRepository extends JpaRepository<BoardMemberComment, Long> {
    List<BoardMemberComment> findByWriterMembershipRequestAndReviewedIsFalse(WriterMembershipRequest writerMembershipRequest);
}
