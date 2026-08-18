package com.vaultbank.dto.response;

import java.math.BigDecimal;

public class TransferResponse {

    private String message;
    private BigDecimal amount;
    private BigDecimal senderBalance;

    public TransferResponse(
            String message,
            BigDecimal amount,
            BigDecimal senderBalance) {

        this.message = message;
        this.amount = amount;
        this.senderBalance = senderBalance;
    }

    public String getMessage() {
        return message;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getSenderBalance() {
        return senderBalance;
    }
}