package com.vaultbank.controller;

import com.vaultbank.dto.response.AccountResponse;
import com.vaultbank.dto.request.TransferRequest;
import com.vaultbank.dto.response.TransferResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import com.vaultbank.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.vaultbank.dto.request.DepositRequest;
import com.vaultbank.dto.response.DepositResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.vaultbank.dto.request.WithdrawRequest;
import com.vaultbank.dto.response.WithdrawResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/users")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/account")
    public ResponseEntity<AccountResponse> getAccount(
            Authentication authentication) {

        String email = authentication.getName();

        AccountResponse account =
                accountService.getAccount(email);

        return ResponseEntity.ok(account);
    }
    @PostMapping("/account/deposit")
    public ResponseEntity<DepositResponse> deposit(
            @Valid @RequestBody DepositRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        DepositResponse response =
                accountService.deposit(email, request);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/account/withdraw")
    public ResponseEntity<WithdrawResponse> withdraw(
            @Valid @RequestBody WithdrawRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        WithdrawResponse response =
                accountService.withdraw(email, request);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/account/transfer")
    public ResponseEntity<TransferResponse> transfer(
            @Valid @RequestBody TransferRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        TransferResponse response =
                accountService.transfer(email, request);

        return ResponseEntity.ok(response);
    }
}