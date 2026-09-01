package com.vaultbank.controller;

import com.vaultbank.dto.request.LoginRequest;
import com.vaultbank.dto.request.ForgotPasswordRequest;
import com.vaultbank.service.OtpService;
import com.vaultbank.dto.request.RegisterRequest;
import com.vaultbank.dto.request.ResetPasswordRequest;
import com.vaultbank.dto.request.VerifyOtpRequest;
import com.vaultbank.dto.response.LoginResponse;
import com.vaultbank.dto.response.UserResponse;
import com.vaultbank.service.AuthService;
import com.vaultbank.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final OtpService otpService;

    public AuthController(
            UserService userService,
            AuthService authService,
            OtpService otpService) {

        this.userService = userService;
        this.authService = authService;
        this.otpService = otpService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(authService.login(request));
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        otpService.generateAndSendOtp(request.getEmail());

        return ResponseEntity.ok(
                "If the email is registered, an OTP has been sent");
    }
    
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request) {

        otpService.verifyOtp(request);

        return ResponseEntity.ok("OTP verified successfully");
    }
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {

        userService.resetPassword(
                request.getEmail(),
                request.getNewPassword()
        );

        return ResponseEntity.ok("Password reset successfully");
    }
}