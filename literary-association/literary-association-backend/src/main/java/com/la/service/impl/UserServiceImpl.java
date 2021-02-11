package com.la.service.impl;

import com.la.model.users.SysUser;
import com.la.repository.UserRepository;
import com.la.service.UserService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository<SysUser> userRepository;

    @Autowired
    private IdentityService identityService;

    @Override
    public SysUser findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = findByUsername(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException(String.format("User with username '%s' was not found", username));
        } else {
            return sysUser;
        }
    }

    @Transactional
    public void createCamundaUser(SysUser sysUser) {
        User camundaUser = identityService.createUserQuery().userIdIn(sysUser.getUsername()).singleResult();
        // If camunda user does not exist
        if (camundaUser == null){
            camundaUser = identityService.newUser(sysUser.getUsername());
            camundaUser.setEmail(sysUser.getEmail());
            camundaUser.setFirstName(sysUser.getFirstName());
            camundaUser.setLastName(sysUser.getLastName());
            camundaUser.setPassword(sysUser.getPassword());
            identityService.saveUser(camundaUser);
        }
    }

}
