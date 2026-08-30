package com.vaultbank.service;

import com.vaultbank.dto.request.DepositRequest;
import com.vaultbank.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import com.vaultbank.dto.request.TransferRequest;
import com.vaultbank.dto.request.WithdrawRequest;

import com.vaultbank.dto.response.AccountResponse;
import com.vaultbank.dto.response.DepositResponse;
import com.vaultbank.dto.response.TransferResponse;
import com.vaultbank.dto.response.WithdrawResponse;

import com.vaultbank.entity.Account;
import com.vaultbank.entity.Transaction;

import com.vaultbank.exception.InsufficientBalanceException;
import com.vaultbank.exception.ResourceNotFoundException;

import com.vaultbank.repository.AccountRepository;
import com.vaultbank.repository.TransactionRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;
import com.vaultbank.dto.response.TransactionResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountServiceImpl(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository) {

        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    // =========================================================
    // GET ACCOUNT
    // =========================================================

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

    // =========================================================
    // DEPOSIT
    // =========================================================

    @Override
    public DepositResponse deposit(
            String email,
            DepositRequest request) {

        Account account = accountRepository.findByUserEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Account not found"));

        BigDecimal depositAmount = request.getAmount();

        BigDecimal newBalance =
                account.getBalance().add(depositAmount);

        // Update account balance
        account.setBalance(newBalance);

        accountRepository.save(account);

        // Create deposit transaction
        Transaction transaction = new Transaction();

        transaction.setReferenceNumber(
                UUID.randomUUID().toString());

        transaction.setAmount(depositAmount);

        transaction.setTransactionType(
                Transaction.TransactionType.DEPOSIT);

        transaction.setStatus(
                Transaction.TransactionStatus.SUCCESS);

        transaction.setAccount(account);

        transaction.setDescription("Cash deposit");

        transactionRepository.save(transaction);

        return new DepositResponse(
                "Amount deposited successfully",
                depositAmount,
                newBalance
        );
    }

    // =========================================================
    // WITHDRAW
    // =========================================================

    @Override
    public WithdrawResponse withdraw(
            String email,
            WithdrawRequest request) {

        Account account = accountRepository.findByUserEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Account not found"));

        BigDecimal withdrawAmount = request.getAmount();

        BigDecimal currentBalance = account.getBalance();

        // Check insufficient balance
        if (withdrawAmount.compareTo(currentBalance) > 0) {

            throw new InsufficientBalanceException(
                    "Insufficient balance");
        }

        BigDecimal newBalance =
                currentBalance.subtract(withdrawAmount);

        // Update account balance
        account.setBalance(newBalance);

        accountRepository.save(account);

        // Create withdrawal transaction
        Transaction transaction = new Transaction();

        transaction.setReferenceNumber(
                UUID.randomUUID().toString());

        transaction.setAmount(withdrawAmount);

        transaction.setTransactionType(
                Transaction.TransactionType.WITHDRAWAL);

        transaction.setStatus(
                Transaction.TransactionStatus.SUCCESS);

        transaction.setAccount(account);

        transaction.setDescription("Cash withdrawal");

        transactionRepository.save(transaction);

        return new WithdrawResponse(
                "Amount withdrawn successfully",
                withdrawAmount,
                newBalance
        );
    }

    // =========================================================
    // TRANSFER
    // =========================================================

    @Override
    public TransferResponse transfer(
            String email,
            TransferRequest request) {

        // 1. Find sender account
        Account senderAccount = accountRepository
                .findByUserEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Sender account not found"));

        // 2. Find receiver account
        Account receiverAccount = accountRepository
                .findByAccountNumber(
                        request.getReceiverAccountNumber())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Receiver account not found"));

        // 3. Prevent transfer to same account
        if (senderAccount.getId()
                .equals(receiverAccount.getId())) {

            throw new IllegalArgumentException(
                    "Cannot transfer money to the same account");
        }

        // 4. Get transfer amount
        BigDecimal transferAmount = request.getAmount();

        // 5. Check sender balance
        if (transferAmount.compareTo(
                senderAccount.getBalance()) > 0) {

            throw new InsufficientBalanceException(
                    "Insufficient balance");
        }

        // 6. Calculate new balances
        BigDecimal senderNewBalance =
                senderAccount.getBalance()
                        .subtract(transferAmount);

        BigDecimal receiverNewBalance =
                receiverAccount.getBalance()
                        .add(transferAmount);

        // 7. Update balances
        senderAccount.setBalance(senderNewBalance);
        receiverAccount.setBalance(receiverNewBalance);

     // 8. Save both accounts
        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        // 9. Create transfer transaction
        Transaction transaction = new Transaction();

        transaction.setReferenceNumber(
                UUID.randomUUID().toString());

        transaction.setAmount(transferAmount);

        transaction.setTransactionType(
                Transaction.TransactionType.TRANSFER);

        transaction.setStatus(
                Transaction.TransactionStatus.SUCCESS);

        transaction.setAccount(senderAccount);

        transaction.setTargetAccount(receiverAccount);

        transaction.setDescription("Account transfer");

        transactionRepository.save(transaction);

        // 10. Return response
        return new TransferResponse(
                "Amount transferred successfully",
                transferAmount,
                senderNewBalance
        );
    }
    @Override
    public Page<TransactionResponse> getTransactions(
            String email,
            String type,
            Pageable pageable) {

        Account account = accountRepository
                .findByUserEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Account not found"));

        Page<Transaction> transactions;

        if (type == null || type.isBlank()) {

            transactions =
                    transactionRepository
                            .findByAccountIdOrderByTransactionDateDesc(
                                    account.getId(),
                                    pageable);

        } else {

            Transaction.TransactionType transactionType;

            try {
                transactionType =
                        Transaction.TransactionType.valueOf(
                                type.toUpperCase());

            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                        "Invalid transaction type: " + type);
            }

            transactions =
                    transactionRepository
                            .findByAccountIdAndTransactionTypeOrderByTransactionDateDesc(
                                    account.getId(),
                                    transactionType,
                                    pageable);
        }

        return transactions.map(transaction -> {

            String targetAccountNumber = null;

            if (transaction.getTargetAccount() != null) {
                targetAccountNumber =
                        transaction.getTargetAccount()
                                .getAccountNumber();
            }

            return new TransactionResponse(
                    transaction.getId(),
                    transaction.getReferenceNumber(),
                    transaction.getAmount(),
                    transaction.getTransactionType().name(),
                    transaction.getStatus().name(),
                    transaction.getDescription(),
                    targetAccountNumber,
                    transaction.getTransactionDate()
            );
        });
    
    }
}