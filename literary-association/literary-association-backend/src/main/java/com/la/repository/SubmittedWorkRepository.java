package com.la.repository;

import com.la.model.registration.SubmittedWork;
import com.la.model.registration.WriterMembershipRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmittedWorkRepository extends JpaRepository<SubmittedWork, Long> {
    List<SubmittedWork> findByPathContaining(String processInstanceIdString);

    List<SubmittedWork> findByWriterMembershipRequest(WriterMembershipRequest writerMembershipRequest);

    void deleteAllByWriterMembershipRequest(WriterMembershipRequest request);
}
