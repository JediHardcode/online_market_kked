package com.gmail.derynem.service.impl;

import com.gmail.derynem.service.MailService;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class MailServiceImpl implements MailService {
    private final static String MESSAGE = "Hello, ur new pass is %s";
    private final MailSender mailSender;

    public MailServiceImpl(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendMessage(String email, String password) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("new Password");
        mailMessage.setText(String.format(MESSAGE, password));
        mailSender.send(mailMessage);
    }
}