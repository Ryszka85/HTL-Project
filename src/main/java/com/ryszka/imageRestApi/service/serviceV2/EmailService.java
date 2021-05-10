package com.ryszka.imageRestApi.service.serviceV2;

import com.ryszka.imageRestApi.security.AppConfigProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmx.com");
        mailSender.setPort(587);

        mailSender.setUsername(AppConfigProperties.EMAIL_ACCOUNT);
        mailSender.setPassword(AppConfigProperties.EMAIL_ACCOUNT_PASSWORD);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
