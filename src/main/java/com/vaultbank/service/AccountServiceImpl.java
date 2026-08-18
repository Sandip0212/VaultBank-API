package com.vaultbank.service;

import com.vaultbank.dto.response.AccountResponse;
import com.vaultbank.entity.Account;
import com.vaultbank.exception.InsufficientBalanceException;
import com.vaultbank.exception.ResourceNotFoundException;
import com.vaultbank.repository.AccountRepository;
import org.springframework.stereotype.Service;
import com.vaultbank.dto.request.DepositRequest;
import com.vaultbank.dto.response.DepositResponse;
import com.vaultbank.entity.Account;
import com.vaultbank.exception.ResourceNotFoundException;

import com.vaultbank.dto.request.WithdrawRequest;
import com.vaultbank.dto.response.WithdrawResponse;
import com.vaultbank.entity.Account;
import com.vaultbank.exception.ResourceNotFoundException;

import java.math.BigDecimal;

import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountResponse getAccount(String email) {

        Account account = accountRepository.findByUserEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Account not found"));

        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getBalance(),
                account.getStatus()
        );
    }
    @Override
    public DepositResponse deposit(String email, DepositRequest request) {

        Account account = accountRepository.findByUserEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Account not found"));

        BigDecimal depositAmount = request.getAmount();

        BigDecimal newBalance =
                account.getBalance().add(depositAmount);

        account.setBalance(newBalance);

        accountRepository.save(account);

        return new DepositResponse(
                "Amount deposited successfully",
                depositAmount,
                newBalance
        );
    }
    
    @Override
    public WithdrawResponse withdraw(
            String email,
            WithdrawRequest request) {

        Account account = accountRepository.findByUserEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Account not found"));

        BigDecimal withdrawAmount = request.getAmount();
        BigDecimal currentBalance = account.getBalance();

        if (withdrawAmount.compareTo(currentBalance) > 0) {
        	throw new InsufficientBalanceException("Insufficient balance");
        }

        BigDecimal newBalance =
                currentBalance.subtract(withdrawAmount);

        account.setBalance(newBalance);

        accountRepository.save(account);

        return new WithdrawResponse(
                "Amount withdrawn successfully",
                withdrawAmount,
                newBalance
        );
    }
    
}