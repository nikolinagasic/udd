package com.la;

import com.la.model.users.SysUser;
import com.la.repository.UserRepository;
import com.la.service.UserService;
import org.camunda.bpm.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class CreateCamundaUsers {

    @Autowired
    UserRepository<SysUser> userRepository;

    @Autowired
    UserService userService;

    @Autowired
    IdentityService identityService;

    @PostConstruct
    public void init(){
        List<SysUser> sysUserList = userRepository.findAll();

        for(SysUser sysUser : sysUserList){
            userService.createCamundaUser(sysUser);
        }

        System.err.println(identityService.createUserQuery().list());
    }
}
