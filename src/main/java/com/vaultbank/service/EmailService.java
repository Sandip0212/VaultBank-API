package com.vaultbank.service;

public interface EmailService {

    void sendOtpEmail(
            String email,
            String otp);
}