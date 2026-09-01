package com.vaultbank.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import com.vaultbank.entity.User;
import com.vaultbank.dto.AdminAccountResponse;
import com.vaultbank.entity.Account;
import com.vaultbank.repository.AccountRepository;

import com.vaultbank.dto.AdminUserResponse;
import com.vaultbank.repository.UserRepository;
import com.vaultbank.service.AccountService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    public AdminController(
            UserRepository userRepository,
            AccountRepository accountRepository,
            AccountService accountService) {

        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    @GetMapping("/test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminTest() {
        return ResponseEntity.ok("Admin access successful");
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdminUserResponse>> getAllUsers() {

        List<AdminUserResponse> users = userRepository.findAll()
                .stream()
                .map(user -> {
                    AdminUserResponse response = new AdminUserResponse();

                    response.setId(user.getId());
                    response.setFullName(user.getFullName());
                    response.setEmail(user.getEmail());
                    response.setPhoneNumber(user.getPhoneNumber());
                    response.setAddress(user.getAddress());
                    response.setEnabled(user.isEnabled());

                    return response;
                })
                .toList();

        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminUserResponse> getUserById(
            @PathVariable Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found with id: " + id));

        AdminUserResponse response = new AdminUserResponse();

        response.setId(user.getId());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setAddress(user.getAddress());
        response.setEnabled(user.isEnabled());

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/accounts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdminAccountResponse>> getAllAccounts() {

        List<AdminAccountResponse> accounts = accountRepository.findAll()
                .stream()
                .map(account -> {

                    AdminAccountResponse response =
                            new AdminAccountResponse();

                    response.setId(account.getId());
                    response.setAccountNumber(account.getAccountNumber());
                    response.setAccountType(
                            account.getAccountType().name());
                    response.setBalance(account.getBalance());
                    response.setStatus(
                            account.getStatus().name());

                    if (account.getUser() != null) {
                        response.setUserId(
                                account.getUser().getId());
                        response.setUserEmail(
                                account.getUser().getEmail());
                    }

                    return response;
                })
                .toList();

        return ResponseEntity.ok(accounts);
    }
    
    @PutMapping("/accounts/{accountId}/freeze")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> freezeAccount(
            @PathVariable Long accountId) {

        accountService.freezeAccount(accountId);

        return ResponseEntity.ok(
                "Account frozen successfully");
    }
}