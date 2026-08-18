package com.vaultbank.dto.response;

import com.vaultbank.entity.Account.AccountStatus;
import com.vaultbank.entity.Account.AccountType;

import java.math.BigDecimal;

public class AccountResponse {

    private Long id;
    private String accountNumber;
    private AccountType accountType;
    private BigDecimal balance;
    private AccountStatus status;

    public AccountResponse(
            Long id,
            String accountNumber,
            AccountType accountType,
            BigDecimal balance,
            AccountStatus status) {

        this.id = id;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public AccountStatus getStatus() {
        return status;
    }
}