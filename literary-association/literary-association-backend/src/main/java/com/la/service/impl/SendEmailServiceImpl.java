package com.la.service.impl;

import com.la.model.Email;
import com.la.service.SendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendEmailServiceImpl implements SendEmailService {

    @Autowired
    private JavaMailSender emailSender;

    public String sendEmail(Email email){
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(email.getEmailFrom());
            message.setTo(email.getEmailTo());
            message.setSubject(email.getSubject());
            message.setText(email.getBody());
            emailSender.send(message);

            return "Your email has been sent to : " + email.getEmailTo();
        }
        catch(Exception e){
            return "There was an exception : " + e.toString();
        }
    }
}
