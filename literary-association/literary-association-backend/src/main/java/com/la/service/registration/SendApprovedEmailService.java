package com.la.service.registration;

import com.la.model.Email;
import com.la.model.users.SysUser;
import com.la.repository.UserRepository;
import com.la.service.SendEmailService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendApprovedEmailService implements JavaDelegate {

    @Autowired
    SendEmailService sendEmailService;

    @Autowired
    UserRepository<SysUser> userRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String username = (String) delegateExecution.getVariable("registeredUser");
        String registeredUserEmail = userRepository.findByUsername(username).getEmail();
        System.err.println("TO REGISTERED USER");
        System.err.println(registeredUserEmail);
        System.err.println(delegateExecution.getProcessInstanceId());

        Email email = new Email();
        email.setSubject("Your writer request has been approved.");
        email.setBody("Well done. :) Please, proceed to fulfill your payment duties for you are obliged to.");
        email.setEmailFrom("rento.office@gmail.com");
        email.setEmailTo("rento.office@gmail.com");
        sendEmailService.sendEmail(email);
    }
}
