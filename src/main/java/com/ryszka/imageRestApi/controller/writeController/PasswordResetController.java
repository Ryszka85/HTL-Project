package com.ryszka.imageRestApi.controller.writeController;

import com.ryszka.imageRestApi.service.serviceV2.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "reset/password")
public class PasswordResetController {
    private EmailService emailService;

    public PasswordResetController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping
    public ResponseEntity<String> sendEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("adrian.ryszka@gmx.net");
        message.setTo("ryszka.a85@htlwienwest.at");
        message.setSubject("some subject");
        message.setText("WOOW it fucking worked");
        emailService.getJavaMailSender()
                .send(message);
        return ResponseEntity.ok("Email was send...");
    }
}
