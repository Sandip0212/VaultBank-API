package com.vaultbank.service;

import com.vaultbank.dto.request.RegisterRequest;
import com.vaultbank.dto.response.UserResponse;

public interface UserService {

    UserResponse register(RegisterRequest request);
}