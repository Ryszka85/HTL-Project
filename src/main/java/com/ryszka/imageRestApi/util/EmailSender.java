package com.ryszka.imageRestApi.util;

import com.ryszka.imageRestApi.service.serviceV2.EmailService;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.net.URI;
import java.net.URISyntaxException;


@Service
public class EmailSender {
    private EmailService emailService;

    public EmailSender(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendVerifyTokenEmail(String subject, String url , String text, String linkName, String to, String linkSuffix) throws MessagingException, URISyntaxException {
        MimeMessage mimeMessage = emailService.getJavaMailSender().createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setFrom("adrian.ryszka@gmx.net");
        helper.setTo(to);
        helper.setSubject(subject);
        StringBuilder sb = new StringBuilder("");
        URI redirectUrl = new URI(url + linkSuffix);
        helper.setText( sb.append("<body>")
                        .append("<h1>Welcome to SpecShots</h1>")
                        .append("<p>").append(text).append("</p>")
                        .append("<a href=\"")
                        .append(redirectUrl.toString())
                        .append("\">")
                        .append(linkName)
                        .append("</a>")
                        .append("</body>")
                        .toString(),
                true  );

        emailService.getJavaMailSender().send(mimeMessage);
    }
}
