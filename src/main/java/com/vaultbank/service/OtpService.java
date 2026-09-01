package com.vaultbank.service;

import com.vaultbank.dto.request.VerifyOtpRequest;

public interface OtpService {

    void generateAndSendOtp(String email);

    void verifyOtp(String email, String otp);
    
    boolean verifyOtp(VerifyOtpRequest request);
}