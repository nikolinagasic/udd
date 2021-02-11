package com.la.service;

import com.la.model.users.SysUser;

public interface UserService {
    SysUser findByUsername(String username);

    void createCamundaUser(SysUser sysUser);
}
