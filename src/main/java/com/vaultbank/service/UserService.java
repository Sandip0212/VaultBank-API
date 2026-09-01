package com.vaultbank.service;

import com.vaultbank.dto.request.RegisterRequest;
import com.vaultbank.dto.request.ChangePasswordRequest;
import com.vaultbank.dto.response.ProfileResponse;
import com.vaultbank.dto.response.UserResponse;

public interface UserService {
	
	ProfileResponse getProfile(String email);

    UserResponse register(RegisterRequest request);
    void changePassword(
            String email,
            ChangePasswordRequest request);
    void resetPassword(String email, String newPassword);
}