package com.vaultbank.dto.response;

import java.math.BigDecimal;

public class DepositResponse {

    private String message;
    private BigDecimal amount;
    private BigDecimal balance;

    public DepositResponse(
            String message,
            BigDecimal amount,
            BigDecimal balance) {
        this.message = message;
        this.amount = amount;
        this.balance = balance;
    }

    public String getMessage() {
        return message;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}