package com.vaultbank.service;

import com.vaultbank.dto.request.TransferRequest;

import com.vaultbank.dto.response.AccountResponse;
import com.vaultbank.entity.Account;
import com.vaultbank.exception.InsufficientBalanceException;
import com.vaultbank.exception.ResourceNotFoundException;
import com.vaultbank.repository.AccountRepository;
import org.springframework.stereotype.Service;
import com.vaultbank.dto.request.DepositRequest;
import com.vaultbank.dto.response.DepositResponse;
import com.vaultbank.dto.response.TransferResponse;
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
    @Override
    public TransferResponse transfer(
            String email,
            TransferRequest request) {

        // 1. Find sender account
        Account senderAccount = accountRepository.findByUserEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Sender account not found"));

        // 2. Find receiver account
        Account receiverAccount = accountRepository
                .findByAccountNumber(request.getReceiverAccountNumber())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Receiver account not found"));

        // 3. Prevent transferring to the same account
        if (senderAccount.getId().equals(receiverAccount.getId())) {
            throw new IllegalArgumentException(
                    "Cannot transfer money to the same account");
        }

        // 4. Get transfer amount
        BigDecimal transferAmount = request.getAmount();

        // 5. Check sender balance
        if (transferAmount.compareTo(senderAccount.getBalance()) > 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance");
        }

        // 6. Calculate new balances
        BigDecimal senderNewBalance =
                senderAccount.getBalance().subtract(transferAmount);

        BigDecimal receiverNewBalance =
                receiverAccount.getBalance().add(transferAmount);

        // 7. Update balances
        senderAccount.setBalance(senderNewBalance);
        receiverAccount.setBalance(receiverNewBalance);

        // 8. Save both accounts
        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        // 9. Return response
        return new TransferResponse(
                "Amount transferred successfully",
                transferAmount,
                senderNewBalance
        );
    }
    
}