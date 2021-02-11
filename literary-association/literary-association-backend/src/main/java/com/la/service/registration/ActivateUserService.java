package com.la.service.registration;

import com.la.model.users.SysUser;
import com.la.repository.UserRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivateUserService implements JavaDelegate {

    @Autowired
    UserRepository<SysUser> userRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String username = (String) delegateExecution.getVariable("registeredUser");

        SysUser user = userRepository.findByUsername(username);
        user.setActive(Boolean.TRUE);

        userRepository.save(user);
    }
}
