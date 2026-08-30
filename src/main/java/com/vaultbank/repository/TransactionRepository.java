package com.vaultbank.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vaultbank.entity.Transaction;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByAccountIdOrderByTransactionDateDesc(
            Long accountId,
            Pageable pageable);

    Page<Transaction>
    findByAccountIdAndTransactionTypeOrderByTransactionDateDesc(
            Long accountId,
            Transaction.TransactionType transactionType,
            Pageable pageable);
}