package com.la.repository;

import com.la.model.MembershipRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipRequestRepository extends JpaRepository<MembershipRequest, Long> {
}
