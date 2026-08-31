package com.vaultbank.service;

import com.vaultbank.dto.request.RegisterRequest;
import com.vaultbank.dto.request.ChangePasswordRequest;
import com.vaultbank.dto.response.ProfileResponse;
import com.vaultbank.dto.response.UserResponse;
import com.vaultbank.entity.User;
import com.vaultbank.exception.DuplicateResourceException;
import com.vaultbank.mapper.UserMapper;
import com.vaultbank.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.vaultbank.entity.Role;
import com.vaultbank.exception.ResourceNotFoundException;
import com.vaultbank.repository.RoleRepository;
import org.springframework.transaction.annotation.Transactional;
import com.vaultbank.entity.Account;
import com.vaultbank.util.AccountNumberGenerator;

import java.math.BigDecimal;
import com.vaultbank.repository.AccountRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    

    public UserServiceImpl(
            UserRepository userRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository,
            AccountRepository accountRepository) {

        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.accountRepository = accountRepository;
    }
    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered");
        }

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new DuplicateResourceException("Phone number already registered");
        }

        User user = userMapper.toEntity(request);
        
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        Role userRole = roleRepository
                .findByName(Role.RoleName.ROLE_USER)
                .orElseThrow(() ->
                        new ResourceNotFoundException("ROLE_USER not found"));
        user.getRoles().add(userRole);
        User savedUser = userRepository.save(user);
        
        Account account = new Account(
                AccountNumberGenerator.generate(),
                Account.AccountType.SAVINGS,
                BigDecimal.ZERO,
                Account.AccountStatus.ACTIVE,
                savedUser
        );
        
        accountRepository.save(account);
        
        return userMapper.toResponse(savedUser);
    }
    @Override
    public ProfileResponse getProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return new ProfileResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.isEnabled(),
                user.getCreatedAt()
        );
    }
    @Override
    public void changePassword(
            String email,
            ChangePasswordRequest request) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));

        // Verify current password
        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword())) {

            throw new IllegalArgumentException(
                    "Current password is incorrect");
        }

        // Prevent using the same password
        if (passwordEncoder.matches(
                request.getNewPassword(),
                user.getPassword())) {

            throw new IllegalArgumentException(
                    "New password must be different from current password");
        }

        // Encode new password
        String encodedPassword =
                passwordEncoder.encode(
                        request.getNewPassword());

        user.setPassword(encodedPassword);

        userRepository.save(user);
    }
}