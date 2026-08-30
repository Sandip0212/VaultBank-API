package com.vaultbank.service;

import com.vaultbank.dto.request.DepositRequest;
import com.vaultbank.dto.request.TransferRequest;
import com.vaultbank.dto.request.WithdrawRequest;

import com.vaultbank.dto.response.AccountResponse;
import com.vaultbank.dto.response.DepositResponse;
import com.vaultbank.dto.response.TransferResponse;
import com.vaultbank.dto.response.WithdrawResponse;
import com.vaultbank.dto.response.TransactionResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.vaultbank.dto.request.CreatePinRequest;
import com.vaultbank.dto.request.UpdatePinRequest;

public interface AccountService {

    AccountResponse getAccount(String email);

    DepositResponse deposit(
            String email,
            DepositRequest request);

    WithdrawResponse withdraw(
            String email,
            WithdrawRequest request);

    TransferResponse transfer(
            String email,
            TransferRequest request);

    Page<TransactionResponse> getTransactions(
            String email,
            String type,
            Pageable pageable);
    void createPin(
            String email,
            CreatePinRequest request);

    void updatePin(
            String email,
            UpdatePinRequest request);
}