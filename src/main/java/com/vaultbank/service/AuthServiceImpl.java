package com.vaultbank.service;

import com.vaultbank.dto.request.LoginRequest;
import com.vaultbank.dto.response.LoginResponse;
import com.vaultbank.dto.response.UserResponse;
import com.vaultbank.entity.User;
import com.vaultbank.exception.ResourceNotFoundException;
import com.vaultbank.mapper.UserMapper;
import com.vaultbank.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.vaultbank.util.JwtUtil;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    
    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper,
            JwtUtil jwtUtil) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
    }
    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Invalid email or password"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new ResourceNotFoundException(
                    "Invalid email or password");
        }

        UserResponse userResponse = userMapper.toResponse(user);

        String token = jwtUtil.generateToken(user.getEmail());

        return new LoginResponse(
                token,
                userResponse
        );
    }
}