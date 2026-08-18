package com.vaultbank.service;

import com.vaultbank.dto.request.DepositRequest;
import com.vaultbank.dto.request.TransferRequest;
import com.vaultbank.dto.request.WithdrawRequest;
import com.vaultbank.dto.response.AccountResponse;
import com.vaultbank.dto.response.DepositResponse;
import com.vaultbank.dto.response.TransferResponse;
import com.vaultbank.dto.response.WithdrawResponse;

public interface AccountService {

    AccountResponse getAccount(String email);

    DepositResponse deposit(String email, DepositRequest request);

    WithdrawResponse withdraw(String email, WithdrawRequest request);

    TransferResponse transfer(String email, TransferRequest request);
}