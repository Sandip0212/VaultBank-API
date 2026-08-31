package com.vaultbank.controller;

import com.vaultbank.dto.request.ChangePasswordRequest;
import com.vaultbank.dto.response.ProfileResponse;
import com.vaultbank.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    // =========================================================
    // GET PROFILE
    // =========================================================

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> profile(
            Authentication authentication) {

        String email = authentication.getName();

        ProfileResponse profile =
                userService.getProfile(email);

        return ResponseEntity.ok(profile);
    }

    // =========================================================
    // CHANGE PASSWORD
    // =========================================================

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        userService.changePassword(email, request);

        return ResponseEntity.ok(
                "Password changed successfully");
    }
}