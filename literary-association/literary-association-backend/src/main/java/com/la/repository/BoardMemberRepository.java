package com.la.repository;

import com.la.model.users.BoardMember;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardMemberRepository extends UserRepository<BoardMember> {
    List<BoardMember> findAll();

    BoardMember findByUsername(String username);
}
