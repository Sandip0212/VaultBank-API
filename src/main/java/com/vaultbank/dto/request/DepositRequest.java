package com.vaultbank.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DepositRequest {

    @NotNull
    @DecimalMin(value = "1.00", message = "Deposit amount must be at least 1")
    private BigDecimal amount;
    
    public BigDecimal getAmount() {
        return amount;
    }
}