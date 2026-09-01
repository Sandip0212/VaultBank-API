package com.vaultbank.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendOtpEmail(
            String email,
            String otp) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("VaultBank - Password Reset OTP");

        message.setText(
                "Your VaultBank password reset OTP is: "
                        + otp
                        + "\n\n"
                        + "This OTP is valid for 5 minutes."
                        + "\n"
                        + "Please do not share this OTP with anyone."
        );

        mailSender.send(message);
    }
}