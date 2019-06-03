package com.gmail.derynem.service.config;

import com.gmail.derynem.service.properties.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@PropertySource("classpath:email.properties")
public class  MailConfig {
    private final MailProperties mailProperties;

    public MailConfig(MailProperties mailProperties) {
        this.mailProperties = mailProperties;
    }

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(mailProperties.getMailHost());
        javaMailSender.setPort(mailProperties.getMailPort());
        javaMailSender.setUsername(mailProperties.getMailUsername());
        javaMailSender.setPassword(mailProperties.getMailPassword());

        Properties mailProperties = javaMailSender.getJavaMailProperties();
        mailProperties.put("mail.transport.protocol", "smtp");
        mailProperties.put("mail.smtp.auth", "true");
        mailProperties.put("mail.smtp.starttls.enable", "true");
        mailProperties.put("mail.debug", "true");
        return javaMailSender;
    }
}