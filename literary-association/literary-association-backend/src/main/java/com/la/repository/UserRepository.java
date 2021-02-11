package com.la.repository;

import com.la.model.users.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository<T extends SysUser> extends JpaRepository<T, Long> {

    SysUser findByUsername(String username);
    List<SysUser> findByType(String editor);
}
