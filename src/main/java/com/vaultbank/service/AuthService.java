package com.vaultbank.service;

import com.vaultbank.dto.request.LoginRequest;
import com.vaultbank.dto.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);
}