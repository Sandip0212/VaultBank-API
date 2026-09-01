package com.vaultbank.service;

import java.time.LocalDateTime;
import com.vaultbank.entity.User;
import com.vaultbank.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaultbank.dto.request.VerifyOtpRequest;
import com.vaultbank.entity.Otp;
import com.vaultbank.exception.ResourceNotFoundException;
import com.vaultbank.repository.OtpRepository;
import com.vaultbank.repository.UserRepository;
import com.vaultbank.dto.request.VerifyOtpRequest;
import com.vaultbank.entity.Otp;
import java.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;
@Service
public class OtpServiceImpl implements OtpService {

    private final OtpRepository otpRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;

    public OtpServiceImpl(
            OtpRepository otpRepository,
            EmailService emailService,
            UserRepository userRepository) {

        this.otpRepository = otpRepository;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }
    // =========================================================
    // GENERATE AND SEND OTP
    // =========================================================

    @Override
    @Transactional
    public void generateAndSendOtp(String email) {
    	
    	 // Check whether email is registered
    	if (!userRepository.existsByEmail(email)) {
    	    return;
    	}

        // Remove previous OTP
        otpRepository.deleteByEmail(email);

        // Generate 6-digit OTP
        String otpCode = String.format(
                "%06d",
                new Random().nextInt(1_000_000)
        );

        Otp otp = Otp.builder()
                .email(email)
                .otp(otpCode)
                .expiresAt(
                        LocalDateTime.now().plusMinutes(5))
                .verified(false)
                .createdAt(LocalDateTime.now())
                .build();

        otpRepository.save(otp);

        // Send OTP through email
        emailService.sendOtpEmail(
                email,
                otpCode);
    }

    // =========================================================
    // VERIFY OTP
    // =========================================================

    @Override
    @Transactional
    public void verifyOtp(
            String email,
            String otpCode) {

        Otp otp = otpRepository
                .findTopByEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "OTP not found"));

        // Check if already verified
        if (otp.isVerified()) {

            throw new IllegalArgumentException(
                    "OTP has already been used");
        }

        // Check expiration
        if (LocalDateTime.now()
                .isAfter(otp.getExpiresAt())) {

            throw new IllegalArgumentException(
                    "OTP has expired");
        }

        // Check OTP
        if (!otp.getOtp().equals(otpCode)) {

            throw new IllegalArgumentException(
                    "Invalid OTP");
        }

        // Mark OTP as verified
        otp.setVerified(true);

        otpRepository.save(otp);
    }
    
    @Override
    @Transactional
    public boolean verifyOtp(VerifyOtpRequest request) {

        Otp otp = otpRepository
                .findTopByEmailOrderByCreatedAtDesc(request.getEmail())
                .orElseThrow(() ->
                        new IllegalArgumentException("OTP not found"));

        if (otp.isVerified()) {
            throw new IllegalArgumentException("OTP already verified");
        }

        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("OTP has expired");
        }

        if (!otp.getOtp().equals(request.getOtp())) {
            throw new IllegalArgumentException("Invalid OTP");
        }

        otp.setVerified(true);
        otpRepository.save(otp);

        return true;
    }
    
   
}