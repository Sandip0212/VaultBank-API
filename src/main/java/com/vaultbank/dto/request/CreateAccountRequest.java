package com.vaultbank.dto.request;

import com.vaultbank.entity.Account.AccountType;
import jakarta.validation.constraints.NotNull;

public class CreateAccountRequest {

    @NotNull(message = "Account type is required")
    private AccountType accountType;

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}