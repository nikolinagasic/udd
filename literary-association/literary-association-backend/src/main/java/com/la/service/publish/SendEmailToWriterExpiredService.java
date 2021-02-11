package com.la.service.publish;

import com.la.model.Email;
import com.la.model.users.SysUser;
import com.la.repository.UserRepository;
import com.la.service.SendEmailService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendEmailToWriterExpiredService implements JavaDelegate {

    @Autowired
    SendEmailService sendEmailService;

    @Autowired
    UserRepository<SysUser> userRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        try {
            String username = (String) delegateExecution.getVariable("writer");
            String writerEmail = userRepository.findByUsername(username).getEmail();
            System.err.println("EXPIRED");
            System.err.println(writerEmail);

            Email email = new Email();
            email.setSubject("Your book request has expired");
            email.setBody("You were given a deadline to submit your script and you DIDN'T SUBMIT IN TIME!");
            email.setEmailFrom("VULKAN");
            email.setEmailTo("rento.office@gmail.com");
            sendEmailService.sendEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BpmnError("EmailFailedToSend");
        }
    }
}