package com.vaultbank.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse {

    private Long id;

    private String referenceNumber;

    private BigDecimal amount;

    private String transactionType;

    private String status;

    private String description;

    private String targetAccountNumber;

    private LocalDateTime transactionDate;

    public TransactionResponse(
            Long id,
            String referenceNumber,
            BigDecimal amount,
            String transactionType,
            String status,
            String description,
            String targetAccountNumber,
            LocalDateTime transactionDate) {

        this.id = id;
        this.referenceNumber = referenceNumber;
        this.amount = amount;
        this.transactionType = transactionType;
        this.status = status;
        this.description = description;
        this.targetAccountNumber = targetAccountNumber;
        this.transactionDate = transactionDate;
    }

    public Long getId() {
        return id;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public String getTargetAccountNumber() {
        return targetAccountNumber;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
}