package com.la.repository;

import com.la.model.registration.WriterMembershipRequest;
import org.springframework.stereotype.Repository;

@Repository
public interface WriterMembershipRequestRepository extends UserRepository<WriterMembershipRequest> {
    WriterMembershipRequest findByUsername(String username);

    void deleteByUsername(String username);
}
