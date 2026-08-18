package com.vaultbank.repository;

import com.vaultbank.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUserEmail(String email);
    Optional<Account> findByAccountNumber(String accountNumber);
}