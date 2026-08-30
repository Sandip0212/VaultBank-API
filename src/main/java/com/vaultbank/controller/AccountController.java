package com.vaultbank.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vaultbank.dto.request.CreatePinRequest;
import com.vaultbank.dto.request.DepositRequest;
import com.vaultbank.dto.request.TransferRequest;
import com.vaultbank.dto.request.UpdatePinRequest;
import com.vaultbank.dto.request.WithdrawRequest;

import com.vaultbank.dto.response.AccountResponse;
import com.vaultbank.dto.response.DepositResponse;
import com.vaultbank.dto.response.TransactionResponse;
import com.vaultbank.dto.response.TransferResponse;
import com.vaultbank.dto.response.WithdrawResponse;

import com.vaultbank.service.AccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // =========================================================
    // GET ACCOUNT
    // =========================================================

    @GetMapping("/account")
    public ResponseEntity<AccountResponse> getAccount(
            Authentication authentication) {

        String email = authentication.getName();

        AccountResponse account =
                accountService.getAccount(email);

        return ResponseEntity.ok(account);
    }

    // =========================================================
    // DEPOSIT
    // =========================================================

    @PostMapping("/account/deposit")
    public ResponseEntity<DepositResponse> deposit(
            @Valid @RequestBody DepositRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        DepositResponse response =
                accountService.deposit(email, request);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // WITHDRAW
    // =========================================================

    @PostMapping("/account/withdraw")
    public ResponseEntity<WithdrawResponse> withdraw(
            @Valid @RequestBody WithdrawRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        WithdrawResponse response =
                accountService.withdraw(email, request);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // TRANSFER
    // =========================================================

    @PostMapping("/account/transfer")
    public ResponseEntity<TransferResponse> transfer(
            @Valid @RequestBody TransferRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        TransferResponse response =
                accountService.transfer(email, request);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // TRANSACTION HISTORY
    // PAGINATION + FILTERING
    // =========================================================

    @GetMapping("/transactions")
    public ResponseEntity<Page<TransactionResponse>> getTransactions(
            Authentication authentication,
            @RequestParam(required = false) String type,
            Pageable pageable) {

        String email = authentication.getName();

        Page<TransactionResponse> transactions =
                accountService.getTransactions(
                        email,
                        type,
                        pageable);

        return ResponseEntity.ok(transactions);
    }

    // =========================================================
    // CREATE PIN
    // =========================================================

    @PostMapping("/account/pin")
    public ResponseEntity<String> createPin(
            @Valid @RequestBody CreatePinRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        accountService.createPin(email, request);

        return ResponseEntity.ok(
                "PIN created successfully");
    }

    // =========================================================
    // UPDATE PIN
    // =========================================================

    @PutMapping("/account/pin")
    public ResponseEntity<String> updatePin(
            @Valid @RequestBody UpdatePinRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        accountService.updatePin(email, request);

        return ResponseEntity.ok(
                "PIN updated successfully");
    }
}