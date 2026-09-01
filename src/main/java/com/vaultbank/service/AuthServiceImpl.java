package com.vaultbank.service;

import com.vaultbank.dto.request.LoginRequest;
import com.vaultbank.entity.RefreshToken;
import com.vaultbank.repository.RefreshTokenRepository;

import java.time.LocalDateTime;
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
    private final RefreshTokenRepository refreshTokenRepository;
    
    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper,
            JwtUtil jwtUtil,
            RefreshTokenRepository refreshTokenRepository) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }
    
    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Invalid email or password"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new ResourceNotFoundException(
                    "Invalid email or password");
        }

        UserResponse userResponse =
                userMapper.toResponse(user);
        String accessToken =
                jwtUtil.generateToken(user.getEmail());

        String refreshToken =
                jwtUtil.generateRefreshToken(user.getEmail());

        RefreshToken refreshTokenEntity = new RefreshToken();

        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setEmail(user.getEmail());
        refreshTokenEntity.setExpiresAt(
                LocalDateTime.now().plusDays(7)
        );
        refreshTokenEntity.setRevoked(false);
        refreshTokenEntity.setCreatedAt(LocalDateTime.now());

        refreshTokenRepository.save(refreshTokenEntity);

        return new LoginResponse(
                accessToken,
                refreshToken,
                userResponse
        );
    }
    
    @Override
    public LoginResponse refreshToken(String refreshToken) {

        if (!jwtUtil.isRefreshToken(refreshToken)) {
            throw new IllegalArgumentException(
                    "Invalid refresh token");
        }

        RefreshToken storedToken = refreshTokenRepository
                .findByToken(refreshToken)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Refresh token not found"));

        if (storedToken.isRevoked()) {
            throw new IllegalArgumentException(
                    "Refresh token has been revoked");
        }

        if (storedToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Refresh token has expired");
        }

        String email;

        try {
            email = jwtUtil.extractEmail(refreshToken);
        } catch (Exception exception) {
            throw new IllegalArgumentException(
                    "Invalid or expired refresh token");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));

        String newAccessToken =
                jwtUtil.generateToken(user.getEmail());

        return new LoginResponse(
                newAccessToken,
                refreshToken,
                userMapper.toResponse(user)
        );
    }
    @Override
    public void logout(String refreshToken) {

        RefreshToken storedToken = refreshTokenRepository
                .findByToken(refreshToken)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Refresh token not found"));

        storedToken.setRevoked(true);

        refreshTokenRepository.save(storedToken);
    }
}